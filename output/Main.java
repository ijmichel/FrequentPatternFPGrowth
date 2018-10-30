import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.List;
import java.util.stream.Stream;
import java.util.Map;
import java.util.StringJoiner;
import java.util.Map.Entry;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Collections;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        FrequentPatternRunner solver = new FrequentPatternRunner();
        solver.solve(1, in, out);
        out.close();
    }

    static class FrequentPatternRunner {
        public void solve(int testNumber, Scanner scanner, PrintWriter out) {
            FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility();

            while (scanner.hasNext()) {
                String support = scanner.nextLine();
                fpUtility.setMinimumSupport(Integer.valueOf(support));

                while (scanner.hasNextLine()) {
                    fpUtility.addTransaction(new FrequentPatternRunner.Transaction(scanner.nextLine()));
                }

            }

            List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();
            List<FrequentPatternRunner.TransactionItemExt> closed = fpUtility.getClosedItems(frequentItems);
            List<FrequentPatternRunner.TransactionItemExt> maxPatterns = fpUtility.getMaximalItems(frequentItems);

            String frequentItemsOutput = "";
            for (FrequentPatternRunner.TransactionItemExt itemExt : frequentItems) {
                frequentItemsOutput = frequentItemsOutput + "\n" + itemExt;
            }
            String closedItemsOutput = "";
            for (FrequentPatternRunner.TransactionItemExt itemExt : closed) {
                closedItemsOutput = closedItemsOutput + "\n" + itemExt;
            }
            String maxItemsOutput = "";
            for (FrequentPatternRunner.TransactionItemExt itemExt : maxPatterns) {
                maxItemsOutput = maxItemsOutput + "\n" + itemExt;
            }

            out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);

        }

        public static class FPUtility {
            private List<FrequentPatternRunner.TransactionItem> frequentOneItemSets = null;
            private List<FrequentPatternRunner.TransactionItem> originalFrequentOneItemSets = null;
            private FrequentPatternRunner.FPTreeNode fpTree = null;
            private List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
            private Integer minimumSupport;

            public FPUtility() {
            }

            public FPUtility(List<FrequentPatternRunner.Transaction> transactions, Integer minimumSupport) {
                this.transactions = transactions;
                this.minimumSupport = minimumSupport;
            }

            public void addTransaction(FrequentPatternRunner.Transaction transcation) {
                transactions.add(transcation);
            }

            public void setMinimumSupport(Integer minimumSupport) {
                this.minimumSupport = minimumSupport;
            }

            public String toString() {
                return "FPUtility{" +
                        "transactions=" + transactions +
                        ", minimumSupport=" + minimumSupport +
                        '}';
            }

            public void generateFrequentOneItemsets() {

                Map<FrequentPatternRunner.TransactionItem, Integer> oneItemSets = new HashMap<>();

                for (FrequentPatternRunner.Transaction transaction : transactions) {
                    List<FrequentPatternRunner.TransactionItem> foundAlready = new ArrayList<>();
                    for (FrequentPatternRunner.TransactionItem transactionItem : transaction.getItems()) {
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

                Map<FrequentPatternRunner.TransactionItem, Integer> kOneFrequentItems = oneItemSets.entrySet().stream()
                        .filter(x -> x.getValue() >= minimumSupport)
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));


                Map<FrequentPatternRunner.TransactionItem, Integer> sortedDescending = kOneFrequentItems
                        .entrySet()
                        .stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

                this.frequentOneItemSets = new ArrayList(sortedDescending.keySet());

                originalFrequentOneItemSets = new ArrayList<>();
                //CLONE!
                for (FrequentPatternRunner.TransactionItem transactionItem : frequentOneItemSets) {
                    String name = transactionItem.getName();
                    Integer support = transactionItem.getSupport();
                    FrequentPatternRunner.TransactionItem newItem = new FrequentPatternRunner.TransactionItem(name);
                    newItem.setSupport(support);
                    this.originalFrequentOneItemSets.add(newItem);
                }

            }

            public List<FrequentPatternRunner.Transaction> getTransactionsSortedByFrequentOne() {
                generateFrequentOneItemsets();

                for (FrequentPatternRunner.Transaction transaction : transactions) {
                    List<FrequentPatternRunner.TransactionItem> transactionItems = transaction.getItems();
                    //Remove items from transaction that are not in frequent list
                    transactionItems.retainAll(this.frequentOneItemSets);
                    //Sort transaction items in same index order
                    Collections.sort(transactionItems, new Comparator<FrequentPatternRunner.TransactionItem>() {
                        public int compare(FrequentPatternRunner.TransactionItem left, FrequentPatternRunner.TransactionItem right) {
                            return Integer.compare(frequentOneItemSets.indexOf(left), frequentOneItemSets.indexOf(right));
                        }
                    });
                    transaction.setItems(transactionItems);
                }

                return transactions;
            }

            public void generateFPTree() {

                FrequentPatternRunner.FPTreeNode currentNode = new FrequentPatternRunner.FPTreeNode();
                List<FrequentPatternRunner.Transaction> frequentTransactions = getTransactionsSortedByFrequentOne();
                for (FrequentPatternRunner.Transaction transaction : frequentTransactions) {
                    currentNode = currentNode.getRoot(currentNode);
                    for (FrequentPatternRunner.TransactionItem transactionItem : transaction.getItems()) {
                        currentNode = currentNode.addChild(transactionItem);
                    }
                }

                FrequentPatternRunner.FPTreeNode root = currentNode.getRoot(currentNode);

                this.fpTree = root;
            }

            public Map<FrequentPatternRunner.TransactionItem, List<FrequentPatternRunner.TransactionItemExt>> getConditionalDBs() {

                //FP Tree
                // {} -> C:3 --> A:2 --> B:1 -> D:1
                //            --> B:1 --> D:1

                Map<FrequentPatternRunner.TransactionItem, List<FrequentPatternRunner.TransactionItemExt>> conditionalDBs = new LinkedHashMap<>();

                generateFPTree();

                for (FrequentPatternRunner.TransactionItem dbItemName : this.originalFrequentOneItemSets) {
                    List<FrequentPatternRunner.FPTreeNode> branchesWithItem = new ArrayList<>();
                    branchesWithItem = fpTree.findChildren(branchesWithItem, dbItemName);
                    List<FrequentPatternRunner.TransactionItemExt> rightHandRules = new ArrayList<>();

                    for (FrequentPatternRunner.FPTreeNode aBranchForItem : branchesWithItem) {
                        FrequentPatternRunner.TransactionItemExt rightHandRule = aBranchForItem.getParentItemsInclusive(dbItemName);

                        if (conditionalDBs.containsKey(dbItemName)) {
                            rightHandRules = conditionalDBs.get(dbItemName);
                        }

                        rightHandRules.add(rightHandRule);
                        conditionalDBs.put(dbItemName, rightHandRules);
                    }


                }

                return conditionalDBs;
            }

            public List<FrequentPatternRunner.TransactionItemExt> getFrequentItems() {
                List<FrequentPatternRunner.TransactionItemExt> frequentItems = new ArrayList<>();
                Map<FrequentPatternRunner.TransactionItem, List<FrequentPatternRunner.TransactionItemExt>> conditionalDBs = getConditionalDBs();
                for (FrequentPatternRunner.TransactionItem transactionItem : conditionalDBs.keySet()) {
                    List<FrequentPatternRunner.TransactionItemExt> frequentItemsForDB = new ArrayList<>();
                    List<FrequentPatternRunner.TransactionItemExt> patternForDB = conditionalDBs.get(transactionItem);

                    FrequentPatternRunner.TransactionItemExt dbItem = new FrequentPatternRunner.TransactionItemExt();
                    dbItem.setDbName(transactionItem.getName());
                    dbItem.setSupport(transactionItem.getSupport());
                    List dbItemList = new ArrayList();
                    dbItemList.add(transactionItem);
                    dbItem.setItems(dbItemList);
                    frequentItemsForDB.add(dbItem);//DB name k=1 is always frequent

                    for (FrequentPatternRunner.TransactionItemExt aPattern : patternForDB) {
                        List<List<FrequentPatternRunner.TransactionItem>> items = getAllItemSetPermutations(aPattern.getItems());

                        for (List<FrequentPatternRunner.TransactionItem> permutations : items) {
                            if (permutations.size() > 1) { //Already added the DB one
                                FrequentPatternRunner.TransactionItemExt permutationExtension = new FrequentPatternRunner.TransactionItemExt();
                                permutationExtension.setItems(permutations);
                                permutationExtension.setDbName(aPattern.getDbName());
                                permutationExtension.setSupport(aPattern.getSupport());

                                if (frequentItemsForDB.contains(permutationExtension)) {
                                    int indexOfExisting = frequentItemsForDB.indexOf(permutationExtension);
                                    FrequentPatternRunner.TransactionItemExt existingPermutation = frequentItemsForDB.get(indexOfExisting);
                                    existingPermutation.setSupport(existingPermutation.getSupport() + permutationExtension.getSupport());
                                } else {
                                    frequentItemsForDB.add(permutationExtension);
                                }
                            }

                        }

                    }

                    for (FrequentPatternRunner.TransactionItemExt permutations : frequentItemsForDB) {
                        if (!frequentItems.contains(permutations)) {
                            frequentItems.add(permutations);
                        }
                    }

                }

                LinkedList<FrequentPatternRunner.TransactionItemExt> minSupportfrequentItems = new LinkedList<>();

                for (FrequentPatternRunner.TransactionItemExt permutations : frequentItems) {
                    if (permutations.getSupport() >= this.minimumSupport) {
                        permutations.getItems().sort(Comparator.comparing(a -> a.getName()));
                        minSupportfrequentItems.add(permutations);
                    }
                }

                minSupportfrequentItems.sort(Comparator.comparing(FrequentPatternRunner.TransactionItemExt::getSupport).reversed()
                        .thenComparing(Comparator.comparing(a -> a.getLine())));

                return minSupportfrequentItems;

            }

            public List<FrequentPatternRunner.TransactionItemExt> getClosedItems(List<FrequentPatternRunner.TransactionItemExt> frequentItems) {
                List<FrequentPatternRunner.TransactionItemExt> closed = new ArrayList<>();

                for (FrequentPatternRunner.TransactionItemExt item : frequentItems) {
                    List<FrequentPatternRunner.TransactionItem> items = item.getItems();

                    boolean closedItemSet = true;
                    for (FrequentPatternRunner.TransactionItemExt itemCheck : frequentItems) {
                        if (itemCheck != item) {
                            if (itemCheck.getItems().containsAll(items)) {
                                if (itemCheck.getSupport() == item.getSupport()) {
                                    closedItemSet = false;
                                }
                            }
                        }
                    }

                    if (closedItemSet) {
                        closed.add(item);
                    }
                }
                return closed;

            }

            public List<FrequentPatternRunner.TransactionItemExt> getMaximalItems(List<FrequentPatternRunner.TransactionItemExt> frequentItems) {
                List<FrequentPatternRunner.TransactionItemExt> maximalItemSets = new ArrayList<>();

                for (FrequentPatternRunner.TransactionItemExt item : frequentItems) {
                    List<FrequentPatternRunner.TransactionItem> items = item.getItems();

                    boolean maximalItemSet = true;
                    for (FrequentPatternRunner.TransactionItemExt itemCheck : frequentItems) {
                        if (itemCheck != item) {
                            if (itemCheck.getItems().containsAll(items)) {
                                maximalItemSet = false;
                            }
                        }
                    }

                    if (maximalItemSet) {
                        maximalItemSets.add(item);
                    }
                }
                return maximalItemSets;

            }

            public List<List<FrequentPatternRunner.TransactionItem>> getAllItemSetPermutations(List<FrequentPatternRunner.TransactionItem> items) {
                List<List<FrequentPatternRunner.TransactionItem>> allPermutations = new ArrayList<List<FrequentPatternRunner.TransactionItem>>();
                getAllItemSetPermutations(0, items, new ArrayList<FrequentPatternRunner.TransactionItem>(), allPermutations);
                return allPermutations;
            }

            private void getAllItemSetPermutations(int k, List<FrequentPatternRunner.TransactionItem> items,
                                                   List<FrequentPatternRunner.TransactionItem> subPerm, List<List<FrequentPatternRunner.TransactionItem>> allPermutations) {
                if (k == items.size()) {
                    allPermutations.add(new ArrayList<>(subPerm));
                    return;
                }
                subPerm.add(items.get(k));

                getAllItemSetPermutations(k + 1, items, subPerm, allPermutations);

                subPerm.remove(subPerm.size() - 1);
                getAllItemSetPermutations(k + 1, items, subPerm, allPermutations);
            }

        }

        public static class Transaction {
            List<FrequentPatternRunner.TransactionItem> items = new ArrayList<FrequentPatternRunner.TransactionItem>();
            private String line;

            public Transaction(String line) {
                this.line = line;
                parseItems(line);
            }

            public List<FrequentPatternRunner.TransactionItem> getItems() {
                return items;
            }

            public void setItems(List<FrequentPatternRunner.TransactionItem> items) {
                this.items = items;
            }

            private void parseItems(String line) {
                String[] itemsStr = line.split(" ");
                List<String> itemList = Arrays.asList(itemsStr);

                for (String anItem : itemList) {
                    if (!items.contains(new FrequentPatternRunner.TransactionItem(anItem))) {
                        items.add(new FrequentPatternRunner.TransactionItem(anItem));
                    }
                }
            }

            public String toString() {
                return "Transaction{" +
                        "items=" + items +
                        ", line='" + line + '\'' +
                        '}';
            }

            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                FrequentPatternRunner.Transaction that = (FrequentPatternRunner.Transaction) o;
                return Objects.equals(items, that.items);
            }

            public int hashCode() {
                return Objects.hash(items);
            }

        }

        public static class TransactionItem {
            String name;
            Integer support;

            public TransactionItem(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public Integer getSupport() {
                return support;
            }

            public void setSupport(Integer support) {
                this.support = support;
            }

            public String toString() {
                return name;
            }

            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                FrequentPatternRunner.TransactionItem that = (FrequentPatternRunner.TransactionItem) o;
                return Objects.equals(name, that.name);
            }

            public int hashCode() {
                return Objects.hash(name);
            }

        }

        public static class TransactionItemExt {
            private List<FrequentPatternRunner.TransactionItem> items;
            private String dbName;
            private String line;
            Integer support = 0;

            public List<FrequentPatternRunner.TransactionItem> getItems() {

                return items;
            }

            public void setItems(List<FrequentPatternRunner.TransactionItem> items) {
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
                for (FrequentPatternRunner.TransactionItem item : items) {
                    this.line = this.line + item.getName();
                }

                return this.line;
            }

            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                FrequentPatternRunner.TransactionItemExt that = (FrequentPatternRunner.TransactionItemExt) o;
                return Objects.equals(items, that.items);
            }

            public int hashCode() {
                return Objects.hash(items, dbName);
            }

            public String toString() {
                String finalOutput = "" + support + " ";

                StringJoiner joiner = new StringJoiner(" ", "[", "]");
                for (FrequentPatternRunner.TransactionItem anItem : items) {
                    joiner.add(anItem.getName());
                }


                finalOutput = finalOutput + joiner.toString();
                return finalOutput;
            }

        }

        public static class FPTreeNode {
            private List<FrequentPatternRunner.FPTreeNode> children = new ArrayList<FrequentPatternRunner.FPTreeNode>();
            private FrequentPatternRunner.FPTreeNode parent = null;
            private FrequentPatternRunner.TransactionItem transactionItem = null;

            public FPTreeNode() {
            }

            public FPTreeNode(FrequentPatternRunner.TransactionItem transactionItem) {
                this.transactionItem = transactionItem;
            }

            public FPTreeNode(FrequentPatternRunner.TransactionItem transactionItem, FrequentPatternRunner.FPTreeNode parent) {
                this.transactionItem = transactionItem;
                this.parent = parent;
            }

            public void setParent(FrequentPatternRunner.FPTreeNode parent) {
                this.parent = parent;
            }

            public FrequentPatternRunner.FPTreeNode addChild(FrequentPatternRunner.TransactionItem transactionItem) {
                FrequentPatternRunner.FPTreeNode child = childHasItem(transactionItem);
                if (child != null) {
                    incrementChildSupportCount(child);
                } else {
                    child = addNewChildWithSupportOne(transactionItem);
                }

                return child;
            }

            private FrequentPatternRunner.FPTreeNode childHasItem(FrequentPatternRunner.TransactionItem transactionItem) {
                for (FrequentPatternRunner.FPTreeNode aChild : children) {
                    if (aChild.getTransactionItem().getName().equals(transactionItem.getName())) {
                        return aChild;
                    }
                }

                return null;
            }

            private FrequentPatternRunner.FPTreeNode addNewChildWithSupportOne(FrequentPatternRunner.TransactionItem transactionItem) {
                transactionItem.setSupport(1);
                FrequentPatternRunner.FPTreeNode child = new FrequentPatternRunner.FPTreeNode(transactionItem);
                child.setParent(this);
                this.children.add(child);

                return child;
            }

            private void incrementChildSupportCount(FrequentPatternRunner.FPTreeNode child) {
                FrequentPatternRunner.TransactionItem transactionItem1 = child.getTransactionItem();
                Integer support = transactionItem1.getSupport();
                support++;
                transactionItem1.setSupport(support);
                child.setTransactionItem(transactionItem1);
            }

            public FrequentPatternRunner.FPTreeNode getParent() {
                return parent;
            }

            public FrequentPatternRunner.TransactionItem getTransactionItem() {
                return transactionItem;
            }

            public void setTransactionItem(FrequentPatternRunner.TransactionItem transactionItem) {
                this.transactionItem = transactionItem;
            }

            public FrequentPatternRunner.FPTreeNode getRoot(FrequentPatternRunner.FPTreeNode current) {
                FrequentPatternRunner.FPTreeNode parent = current.getParent();
                if (parent == null) {
                    return current;
                } else {
                    return getRoot(parent);
                }
            }

            public List<FrequentPatternRunner.FPTreeNode> findChildren(List<FrequentPatternRunner.FPTreeNode> children, FrequentPatternRunner.TransactionItem transactionItem) {
                for (FrequentPatternRunner.FPTreeNode aChild : this.children) {
                    if (aChild.getTransactionItem().getName().equals(transactionItem.getName())) {
                        children.add(aChild);
                    }

                    aChild.findChildren(children, transactionItem);
                }

                return children;
            }

            public FrequentPatternRunner.TransactionItemExt getParentItemsInclusive(FrequentPatternRunner.TransactionItem transactionItem) {
                FrequentPatternRunner.TransactionItemExt extension = new FrequentPatternRunner.TransactionItemExt();
                extension.setDbName(transactionItem.getName());
                extension.setSupport(this.getTransactionItem().getSupport());
                List<FrequentPatternRunner.TransactionItem> branchPatterns = new ArrayList<>();
                FrequentPatternRunner.FPTreeNode parent = this;

                while (parent != null) {
                    if (parent.getTransactionItem() != null) {
                        branchPatterns.add(parent.getTransactionItem());
                    }
                    parent = parent.getParent();
                }
                ;

                extension.setItems(branchPatterns);

                return extension;
            }

            public String toString() {
                return "FPTreeNode{" +
                        "children=" + children +
                        ", transactionItem=" + transactionItem +
                        '}';
            }

        }

    }
}

