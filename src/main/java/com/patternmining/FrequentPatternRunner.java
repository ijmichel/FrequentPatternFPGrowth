package com.patternmining;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * IMICHEL 10/29/2018 - Done for Homework 3 of CSE 412 - Data Mining
 */
public class FrequentPatternRunner {
    public void solve(int testNumber, Scanner scanner, PrintWriter out) {
        FPUtility fpUtility = new FPUtility();

        while (scanner.hasNext()) {
            String support = scanner.nextLine();
            fpUtility.setMinimumSupport(Integer.valueOf(support));

            while (scanner.hasNextLine()) {
                fpUtility.addTransaction(new Transaction(scanner.nextLine()));
            }

        }

        List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();
        List<FrequentPatternRunner.TransactionItemExt> closed = fpUtility.getClosedItems(frequentItems);
        List<FrequentPatternRunner.TransactionItemExt> maxPatterns = fpUtility.getMaximalItems(frequentItems);

        String frequentItemsOutput = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: frequentItems) {
            frequentItemsOutput = frequentItemsOutput + "\n" + itemExt;
        }
        String closedItemsOutput = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: closed) {
            closedItemsOutput = closedItemsOutput + "\n" + itemExt;
        }
        String maxItemsOutput = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: maxPatterns) {
            maxItemsOutput = maxItemsOutput + "\n" + itemExt;
        }

        out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);

    }

    public static class FPUtility {
        private List<TransactionItem>  frequentOneItemSets = null;
        private List<TransactionItem>  originalFrequentOneItemSets = null;
        private FPTreeNode fpTree = null;


        public FPUtility() {
        }

        public FPUtility(List<Transaction> transactions, Integer minimumSupport) {
            this.transactions = transactions;
            this.minimumSupport = minimumSupport;
        }

        private List<Transaction> transactions = new ArrayList<>();

        private Integer minimumSupport;

        public void addTransaction(Transaction transcation) {
            transactions.add(transcation);
        }

        public Integer getMinimumSupport() {
            return minimumSupport;
        }

        public void setMinimumSupport(Integer minimumSupport) {
            this.minimumSupport = minimumSupport;
        }

        @Override
        public String toString() {
            return "FPUtility{" +
                    "transactions=" + transactions +
                    ", minimumSupport=" + minimumSupport +
                    '}';
        }

        public void generateFrequentOneItemsets() {

            Map<TransactionItem, Integer> oneItemSets = new HashMap<>();

            for (Transaction transaction : transactions) {
                List<TransactionItem> foundAlready = new ArrayList<>();
                for (TransactionItem transactionItem : transaction.getItems()) {
                    if (oneItemSets.containsKey(transactionItem)) {
                        if (!foundAlready.contains(transactionItem)) {
                            foundAlready.add(transactionItem);
                            Integer count = oneItemSets.get(transactionItem);
                            count = count + 1;
                            transactionItem.setSupport(count);
                            oneItemSets.remove(transactionItem);
                            oneItemSets.put(transactionItem, count);
                        }
                    } else {
                        oneItemSets.put(transactionItem, 1);
                        transactionItem.setSupport(1);
                    }
                }
            }

            Map<TransactionItem, Integer> kOneFrequentItems = oneItemSets.entrySet().stream()
                    .filter(x -> x.getValue() >= minimumSupport)
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));


            Map<TransactionItem, Integer> sortedDescending = kOneFrequentItems
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

            this.frequentOneItemSets = new ArrayList(sortedDescending.keySet());

            originalFrequentOneItemSets = new ArrayList<>();
            //CLONE!
            for (TransactionItem transactionItem : frequentOneItemSets){
                String name = transactionItem.getName();
                Integer support = transactionItem.getSupport();
                TransactionItem newItem = new TransactionItem(name);
                newItem.setSupport(support);
                this.originalFrequentOneItemSets.add(newItem);
            }

        }

        public List<Transaction> getTransactionsSortedByFrequentOne() {
            generateFrequentOneItemsets();

            for (Transaction transaction : transactions) {
                List<TransactionItem> transactionItems = transaction.getItems();
                //Remove items from transaction that are not in frequent list
                transactionItems.retainAll(this.frequentOneItemSets);
                //Sort transaction items in same index order
                Collections.sort(transactionItems, new Comparator<TransactionItem>() {
                    public int compare(TransactionItem left, TransactionItem right) {
                        return Integer.compare(frequentOneItemSets.indexOf(left), frequentOneItemSets.indexOf(right));
                    }
                });
                transaction.setItems(transactionItems);
            }

            return transactions;
        }

        public void generateFPTree() {

            FPTreeNode currentNode = new FPTreeNode();
            List<Transaction> frequentTransactions = getTransactionsSortedByFrequentOne();
            for (Transaction transaction : frequentTransactions) {
                currentNode = currentNode.getRoot(currentNode);
                for (TransactionItem transactionItem : transaction.getItems()) {
                    currentNode = currentNode.addChild(transactionItem);
                }
            }

            FPTreeNode root = currentNode.getRoot(currentNode);

            this.fpTree = root;
        }

        public Map<TransactionItem, List<TransactionItemExt>> getConditionalDBs() {

            //FP Tree
            // {} -> C:3 --> A:2 --> B:1 -> D:1
            //            --> B:1 --> D:1

            Map<TransactionItem, List<TransactionItemExt>> conditionalDBs = new LinkedHashMap<>();

            generateFPTree();

            for (TransactionItem dbItemName : this.originalFrequentOneItemSets) {
                List<FPTreeNode> branchesWithItem = new ArrayList<>();
                branchesWithItem = fpTree.findChildren(branchesWithItem, dbItemName);
                List<TransactionItemExt> rightHandRules = new ArrayList<>();

                for (FPTreeNode aBranchForItem : branchesWithItem) {
                    TransactionItemExt rightHandRule = aBranchForItem.getParentItemsInclusive(dbItemName);

                    if(conditionalDBs.containsKey(dbItemName)){
                        rightHandRules = conditionalDBs.get(dbItemName);
                    }

                    rightHandRules.add(rightHandRule);
                    conditionalDBs.put(dbItemName,rightHandRules);
                }


            }

            return conditionalDBs;
        }

        public List<TransactionItemExt> getFrequentItems(){
            List<TransactionItemExt> frequentItems = new ArrayList<>();
            Map<TransactionItem, List<TransactionItemExt>> conditionalDBs = getConditionalDBs();
            for (TransactionItem transactionItem : conditionalDBs.keySet()) {
                List<TransactionItemExt> frequentItemsForDB = new ArrayList<>();
                List<TransactionItemExt> patternForDB = conditionalDBs.get(transactionItem);

                TransactionItemExt dbItem = new TransactionItemExt();
                dbItem.setDbName(transactionItem.getName());
                dbItem.setSupport(transactionItem.getSupport());
                List dbItemList = new ArrayList();
                dbItemList.add(transactionItem);
                dbItem.setItems(dbItemList);
                frequentItemsForDB.add(dbItem);//DB name k=1 is always frequent

                for (TransactionItemExt aPattern : patternForDB) {
                    List<List<TransactionItem>> items = getAllItemSetPermutations(aPattern.getItems());

                    for (List<TransactionItem> permutations :items) {
                        if(permutations.size() > 1){ //Already added the DB one
                            TransactionItemExt permutationExtension = new TransactionItemExt();
                            permutationExtension.setItems(permutations);
                            permutationExtension.setDbName(aPattern.getDbName());
                            permutationExtension.setSupport(aPattern.getSupport());

                            if(frequentItemsForDB.contains(permutationExtension)){
                                int indexOfExisting = frequentItemsForDB.indexOf(permutationExtension);
                                TransactionItemExt existingPermutation = frequentItemsForDB.get(indexOfExisting);
                                existingPermutation.setSupport(existingPermutation.getSupport() + permutationExtension.getSupport());
                            }else{
                                frequentItemsForDB.add(permutationExtension);
                            }
                        }

                    }

                }

                for (TransactionItemExt permutations: frequentItemsForDB) {
                    if(!frequentItems.contains(permutations)){
                        frequentItems.add(permutations);
                    }
                }

            }

            LinkedList<TransactionItemExt> minSupportfrequentItems = new LinkedList<>();

            for (TransactionItemExt permutations: frequentItems) {
                if(permutations.getSupport() >= this.minimumSupport){
                    permutations.getItems().sort(Comparator.comparing(a -> a.getName()));
                    minSupportfrequentItems.add(permutations);
                }
            }

            minSupportfrequentItems.sort(Comparator.comparing(TransactionItemExt::getSupport).reversed()
                    .thenComparing(Comparator.comparing(a -> a.getLine())));

            return minSupportfrequentItems;

        }

        public List<TransactionItemExt> getClosedItems(List<TransactionItemExt> frequentItems){
            List<TransactionItemExt> closed = new ArrayList<>();

            for (TransactionItemExt item: frequentItems) {
                List<TransactionItem> items = item.getItems();

                boolean closedItemSet = true;
                for (TransactionItemExt itemCheck: frequentItems) {
                    if(itemCheck != item){
                        if(itemCheck.getItems().containsAll(items)){
                            if(itemCheck.getSupport() == item.getSupport()){
                                closedItemSet = false;
                            }
                        }
                    }
                }

                if(closedItemSet){
                    closed.add(item);
                }
            }
            return closed;

        }

        public List<TransactionItemExt> getMaximalItems(List<TransactionItemExt> frequentItems){
            List<TransactionItemExt> maximalItemSets = new ArrayList<>();

            for (TransactionItemExt item: frequentItems) {
                List<TransactionItem> items = item.getItems();

                boolean maximalItemSet = true;
                for (TransactionItemExt itemCheck: frequentItems) {
                    if(itemCheck != item){
                        if(itemCheck.getItems().containsAll(items)){
                            maximalItemSet = false;
                        }
                    }
                }

                if(maximalItemSet){
                    maximalItemSets.add(item);
                }
            }
            return maximalItemSets;

        }

        public List<List<TransactionItem>> getAllItemSetPermutations(List<TransactionItem> items) {
            List<List<TransactionItem>> allPermutations = new ArrayList<List<TransactionItem>>();
            getAllItemSetPermutations(0,items, new ArrayList<TransactionItem>(), allPermutations);
            return allPermutations;
        }

        private void getAllItemSetPermutations(int k, List<TransactionItem> items,
                                               List<TransactionItem> subPerm, List<List<TransactionItem>> allPermutations) {
            if (k == items.size()) {
                allPermutations.add(new ArrayList<>(subPerm));
                return;
            }
            subPerm.add(items.get(k));

            getAllItemSetPermutations(k + 1,items,subPerm, allPermutations);

            subPerm.remove(subPerm.size() - 1);
            getAllItemSetPermutations(k + 1,items , subPerm, allPermutations);
        }

        public List<TransactionItem> getFrequentOneItemSets() {
            return frequentOneItemSets;
        }


        public FPTreeNode getFpTree() {
            return fpTree;
        }

        public void setFpTree(FPTreeNode fpTree) {
            this.fpTree = fpTree;
        }
    }

    public static class Transaction {
        List<TransactionItem> items = new ArrayList<TransactionItem>();

        public Transaction(String line) {
            this.line = line;
            parseItems(line);
        }

        private String line;

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public List<TransactionItem> getItems() {
            return items;
        }

        public void setItems(List<TransactionItem> items) {
            this.items = items;
        }

        private void parseItems(String line) {
            String[] itemsStr = line.split(" ");
            List<String> itemList = Arrays.asList(itemsStr);

            for (String anItem : itemList) {
                if(!items.contains(new TransactionItem(anItem))) {
                    items.add(new TransactionItem(anItem));
                }
            }
        }


        @Override
        public String toString() {
            return "Transaction{" +
                    "items=" + items +
                    ", line='" + line + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Transaction that = (Transaction) o;
            return Objects.equals(items, that.items);
        }

        @Override
        public int hashCode() {
            return Objects.hash(items);
        }
    }

    public static class TransactionItem {
        public TransactionItem(String name) {
            this.name = name;
        }

        String name;
        Integer support;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getSupport() {
            return support;
        }

        public void setSupport(Integer support) {
            this.support = support;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TransactionItem that = (TransactionItem) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

    }

    public static class TransactionItemExt{
        private List<TransactionItem> items;
        private String dbName;
        private String line;
        Integer support = 0;

        public List<TransactionItem> getItems() {

            return items;
        }

        public void setItems(List<TransactionItem> items) {
            this.items = items;
        }

        public Integer getSupport() {
            return support;
        }

        public void setSupport(Integer support) {
            this.support = support;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public String getLine() {
            this.line = "";
            for (TransactionItem item: items) {
                this.line  = this.line  + item.getName();
            }

            return this.line;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TransactionItemExt that = (TransactionItemExt) o;
            return Objects.equals(items, that.items);
        }

        @Override
        public int hashCode() {
            return Objects.hash(items, dbName);
        }

        @Override
        public String toString() {
            String finalOutput = "" + support + " ";

            StringJoiner joiner = new StringJoiner(" ", "[", "]");
            for (TransactionItem anItem: items) {
                joiner.add(anItem.getName());
            }


            finalOutput = finalOutput + joiner.toString();
            return finalOutput;
        }
    }

    public static class FPTreeNode {
        private List<FPTreeNode> children = new ArrayList<FPTreeNode>();
        private FPTreeNode parent = null;
        private TransactionItem transactionItem = null;

        public FPTreeNode() {
        }

        public FPTreeNode(TransactionItem transactionItem) {
            this.transactionItem = transactionItem;
        }

        public FPTreeNode(TransactionItem transactionItem, FPTreeNode parent) {
            this.transactionItem = transactionItem;
            this.parent = parent;
        }

        public List<FPTreeNode> getChildren() {
            return children;
        }

        public void setParent(FPTreeNode parent) {
            this.parent = parent;
        }

        public FPTreeNode addChild(TransactionItem transactionItem) {
            FPTreeNode child = childHasItem(transactionItem);
            if (child != null) {
                incrementChildSupportCount(child);
            } else {
                child = addNewChildWithSupportOne(transactionItem);
            }

            return child;
        }

        private FPTreeNode childHasItem(TransactionItem transactionItem) {
            for (FPTreeNode aChild : children) {
                if (aChild.getTransactionItem().getName().equals(transactionItem.getName())) {
                    return aChild;
                }
            }

            return null;
        }

        private FPTreeNode addNewChildWithSupportOne(TransactionItem transactionItem) {
            transactionItem.setSupport(1);
            FPTreeNode child = new FPTreeNode(transactionItem);
            child.setParent(this);
            this.children.add(child);

            return child;
        }

        private void incrementChildSupportCount(FPTreeNode child) {
            TransactionItem transactionItem1 = child.getTransactionItem();
            Integer support = transactionItem1.getSupport();
            support++;
            transactionItem1.setSupport(support);
            child.setTransactionItem(transactionItem1);
        }

        public void addChild(FPTreeNode child) {
            child.setParent(this);
            this.children.add(child);
        }

        public FPTreeNode getParent() {
            return parent;
        }

        public TransactionItem getTransactionItem() {
            return transactionItem;
        }

        public void setTransactionItem(TransactionItem transactionItem) {
            this.transactionItem = transactionItem;
        }

        public FPTreeNode getRoot(FPTreeNode current) {
            FPTreeNode parent = current.getParent();
            if (parent == null) {
                return current;
            } else {
                return getRoot(parent);
            }
        }

        public List<FPTreeNode> findChildren(List<FPTreeNode> children, TransactionItem transactionItem) {
            for (FPTreeNode aChild : this.children) {
                if (aChild.getTransactionItem().getName().equals(transactionItem.getName())) {
                    children.add(aChild);
                }

                aChild.findChildren(children, transactionItem);
            }

            return children;
        }

        public TransactionItemExt getParentItemsInclusive(TransactionItem transactionItem) {
            TransactionItemExt extension = new TransactionItemExt();
            extension.setDbName(transactionItem.getName());
            extension.setSupport(this.getTransactionItem().getSupport());
            List<TransactionItem> branchPatterns = new ArrayList<>();
            FPTreeNode parent = this;

            while (parent != null) {
                if (parent.getTransactionItem() != null) {
                    branchPatterns.add(parent.getTransactionItem());
                }
                parent = parent.getParent();
            };

            extension.setItems(branchPatterns);

            return extension;
        }


        @Override
        public String toString() {
            return "FPTreeNode{" +
                    "children=" + children +
                    ", transactionItem=" + transactionItem +
                    '}';
        }
    }
}
