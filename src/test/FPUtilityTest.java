import com.patternmining.FrequentPatternRunner;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FPUtilityTest {

    @Test
    public void generateItemSets_Withk_1_ShouldGenerate_4_items(){
        //B A C E D
        //A C
        //C B D

        //B: 2
        //A: 2
        //C: 3
        //E: 1
        //D: 2

        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

        System.out.println(fpUtility);

       fpUtility.generateFrequentOneItemsets();;

        Assert.assertNotNull(fpUtility.getFrequentOneItemSets());
        Assert.assertEquals(4,fpUtility.getFrequentOneItemSets().size());
        Assert.assertEquals("C",fpUtility.getFrequentOneItemSets().get(0).getName());
        Assert.assertEquals("A",fpUtility.getFrequentOneItemSets().get(1).getName());
        Assert.assertEquals("B",fpUtility.getFrequentOneItemSets().get(2).getName());
        Assert.assertEquals("D",fpUtility.getFrequentOneItemSets().get(3).getName());

    }

    @Test
    public void generateItemSets_Withk_1_ShouldGenerate_3_items(){
        //B C E D
        //A C
        //C B D

        //B: 2
        //A: 1
        //C: 3
        //E: 1
        //D: 2

        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

        fpUtility.generateFrequentOneItemsets();

        Assert.assertNotNull(fpUtility.getFrequentOneItemSets());
        Assert.assertEquals(3,fpUtility.getFrequentOneItemSets().size());
        Assert.assertEquals("C",fpUtility.getFrequentOneItemSets().get(0).getName());
        Assert.assertEquals("B",fpUtility.getFrequentOneItemSets().get(1).getName());
        Assert.assertEquals("D",fpUtility.getFrequentOneItemSets().get(2).getName());
    }

    @Test
    public void getTransactionsSorted_WithInputSampleList_ShouldReturnCorrectOrderedOnes(){
        //B C E D F
        //A C F
        //C B D F

        //B: 2
        //A: 1 //
        //C: 3
        //E: 1 //
        //D: 2
        //F: 3

        // C F B D

        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B C E D F");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C F");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D F");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

        List<FrequentPatternRunner.Transaction> actualTransactionsSorted = fpUtility.getTransactionsSortedByFrequentOne();

        Assert.assertNotNull(actualTransactionsSorted);
        Assert.assertEquals(3,actualTransactionsSorted.size());

        FrequentPatternRunner.Transaction expected1 = new FrequentPatternRunner.Transaction("C F B D");
        FrequentPatternRunner.Transaction actual1 = actualTransactionsSorted.get(0);
        Assert.assertEquals(expected1,actual1);

        expected1 = new FrequentPatternRunner.Transaction("C F");
        actual1 = actualTransactionsSorted.get(1);
        Assert.assertEquals(expected1,actual1);

        expected1 = new FrequentPatternRunner.Transaction("C F B D");
        actual1 = actualTransactionsSorted.get(2);
        Assert.assertEquals(expected1,actual1);
    }

    @Test
    public void getTransactionsSorted_WithRealSampleInput_ShouldReturnCorrectOrderedOnes(){
        //B A C E D
        //A C
        //C B D

        //B: 2
        //A: 2
        //C: 3
        //E: 1
        //D: 2

        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

        List<FrequentPatternRunner.Transaction> actualTransactionsSorted = fpUtility.getTransactionsSortedByFrequentOne();

        Assert.assertNotNull(actualTransactionsSorted);
        Assert.assertEquals(3,actualTransactionsSorted.size());

        FrequentPatternRunner.Transaction expected1 = new FrequentPatternRunner.Transaction("C A B D");
        FrequentPatternRunner.Transaction actual1 = actualTransactionsSorted.get(0);
        Assert.assertEquals(expected1,actual1);

        expected1 = new FrequentPatternRunner.Transaction("C A");
        actual1 = actualTransactionsSorted.get(1);
        Assert.assertEquals(expected1,actual1);

        expected1 = new FrequentPatternRunner.Transaction("C B D");
        actual1 = actualTransactionsSorted.get(2);
        Assert.assertEquals(expected1,actual1);
    }

    @Test
    public void getTransactionsSorted_WithRealSampleInput_WithSupport_3_ShouldReturnCorrectOrderedOnes(){
        //B A C E D
        //A C
        //C B D

        //B: 2
        //A: 2
        //C: 3
        //E: 1
        //D: 2

        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,3);

        List<FrequentPatternRunner.Transaction> actualTransactionsSorted = fpUtility.getTransactionsSortedByFrequentOne();

        Assert.assertNotNull(actualTransactionsSorted);
        Assert.assertEquals(3,actualTransactionsSorted.size());

        FrequentPatternRunner.Transaction expected1 = new FrequentPatternRunner.Transaction("C");
        FrequentPatternRunner.Transaction actual1 = actualTransactionsSorted.get(0);
        Assert.assertEquals(expected1,actual1);

        expected1 = new FrequentPatternRunner.Transaction("C");
        actual1 = actualTransactionsSorted.get(1);
        Assert.assertEquals(expected1,actual1);

        expected1 = new FrequentPatternRunner.Transaction("C");
        actual1 = actualTransactionsSorted.get(2);
        Assert.assertEquals(expected1,actual1);
    }

    @Test
    public void getFPTree_WithSampleTransactions_ShouldReturnCorrectTree(){
        //B A C E D
        //A C
        //C B D

        //B: 2
        //A: 2
        //C: 3
        //E: 1
        //D: 2

        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

        fpUtility.generateFPTree();

        //FP Tree
        // {} -> C:3 --> A:2 --> B:1 -> D:1
        //            --> B:1 --> D:1

        //C:3
        Assert.assertNotNull(fpUtility.getFpTree());
        Assert.assertNull(fpUtility.getFpTree().getParent());
        Assert.assertTrue(fpUtility.getFpTree().getChildren().size()==1);
        FrequentPatternRunner.FPTreeNode rootC = fpUtility.getFpTree().getChildren().get(0);
        Assert.assertEquals("C",rootC.getTransactionItem().getName());
        Assert.assertTrue(rootC.getTransactionItem().getSupport()==3);

        //Branch 1 off C--------
        //A:2
        Assert.assertEquals(2,rootC.getChildren().size());
        FrequentPatternRunner.FPTreeNode childA = rootC.getChildren().get(0);
        Assert.assertEquals("A",childA.getTransactionItem().getName());
        Assert.assertTrue(childA.getTransactionItem().getSupport()==2);

        //B:1
        Assert.assertEquals(1,childA.getChildren().size());
        FrequentPatternRunner.FPTreeNode childB = childA.getChildren().get(0);
        Assert.assertEquals("B",childB.getTransactionItem().getName());
        Assert.assertTrue(childB.getTransactionItem().getSupport()==1);

        //B:1
        Assert.assertEquals(1,childB.getChildren().size());
        FrequentPatternRunner.FPTreeNode childD = childB.getChildren().get(0);
        Assert.assertEquals("D",childD.getTransactionItem().getName());
        Assert.assertTrue(childD.getTransactionItem().getSupport()==1);

        ///Branch 2 off C-------

        //B:1
        FrequentPatternRunner.FPTreeNode childB_2 = rootC.getChildren().get(1);
        Assert.assertEquals("B",childB_2.getTransactionItem().getName());
        Assert.assertTrue(childB_2.getTransactionItem().getSupport()==1);

        //D:1
        FrequentPatternRunner.FPTreeNode childD_2 = childB_2.getChildren().get(0);
        Assert.assertEquals("D",childD_2.getTransactionItem().getName());
        Assert.assertTrue(childD_2.getTransactionItem().getSupport()==1);
    }

    @Test
    public void findChildren_WithSampleFPTree_ShouldReturnChildrenOfSubsetOfTree(){

        //B A C E D
        //A C
        //C B D

        //B: 2
        //A: 2
        //C: 3
        //E: 1
        //D: 2

        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

        //FP Tree
        // {} -> C:3 --> A:2 --> B:1 -> D:1
        //            --> B:1 --> D:1

        fpUtility.generateFPTree();

        List<FrequentPatternRunner.FPTreeNode> children = new ArrayList<>();

        List<FrequentPatternRunner.TransactionItem> items = fpUtility.getFrequentOneItemSets();
        List<FrequentPatternRunner.FPTreeNode> childrenOf = fpUtility.getFpTree().findChildren(children,items.get(2));

       Assert.assertTrue(children.size()==2);

    }

    @Test
    public void getConditionalDBs(){

        //B A C E D
        //A C
        //C B D

        //B: 2
        //A: 2
        //C: 3
        //E: 1
        //D: 2

        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);
        Map<FrequentPatternRunner.TransactionItem, List<FrequentPatternRunner.TransactionItemExt>> conditionalDBs = fpUtility.getConditionalDBs();

        Assert.assertNotNull(conditionalDBs);
    }



    @Test
    public void getFrequentItems_test_allPermutations() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);
        fpUtility.getConditionalDBs();
        List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();

        Assert.assertNotNull(frequentItems);

        String output = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: frequentItems) {
            output = output + "\n" + itemExt;
        }

        Assert.assertEquals(
                "\n3 [C]\n" +
                "2 [A]\n" +
                "2 [A C]\n" +
                "2 [B]\n" +
                "2 [B C]\n" +
                "2 [B C D]\n" +
                "2 [B D]\n" +
                "2 [C D]\n" +
                "2 [D]",output);
    }

    @Test
    public void getFrequentItems_test_allPermutations_test2() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("data mining");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("frequent pattern mining");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("mining frequent patterns from the transaction dataset");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("closed and maximal pattern mining");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);
        List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();

        Assert.assertNotNull(frequentItems);

        String output = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: frequentItems) {
            output = output + "\n" + itemExt;
        }

        Assert.assertEquals(
                "\n4 [mining]\n" +
                        "2 [frequent]\n" +
                        "2 [frequent mining]\n" +
                        "2 [mining pattern]\n" +
                        "2 [pattern]",output);
    }

    @Test
    public void getClosedItemset_Permutations_Test1() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);
        fpUtility.getConditionalDBs();
        List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();


        List<FrequentPatternRunner.TransactionItemExt> closed = fpUtility.getClosedItems(frequentItems);

        Assert.assertNotNull(closed);

        String output = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: closed) {
            output = output + "\n" + itemExt;
        }

        Assert.assertEquals(
                "\n3 [C]\n" +
                        "2 [A C]\n" +
                        "2 [B C D]",output);

    }

    @Test
    public void getClosedItemset_Permutations_Test2() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("data mining");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("frequent pattern mining");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("mining frequent patterns from the transaction dataset");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("closed and maximal pattern mining");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);
        fpUtility.getConditionalDBs();
        List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();


        List<FrequentPatternRunner.TransactionItemExt> closed = fpUtility.getClosedItems(frequentItems);

        Assert.assertNotNull(closed);

        String output = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: closed) {
            output = output + "\n" + itemExt;
        }

        Assert.assertEquals(
                "\n4 [mining]\n" +
                        "2 [frequent mining]\n" +
                        "2 [mining pattern]",output);

    }

    @Test
    public void getMaximalItemset_Permutations_Test1() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);
        fpUtility.getConditionalDBs();
        List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();


        List<FrequentPatternRunner.TransactionItemExt> closed = fpUtility.getMaximalItems(frequentItems);

        Assert.assertNotNull(closed);

        String output = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: closed) {
            output = output + "\n" + itemExt;
        }

        Assert.assertEquals(
                "\n2 [A C]\n" +
                        "2 [B C D]",output);

    }


    @Test
    public void getMaximalItemset_Permutations_Test2() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("data mining");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("frequent pattern mining");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("mining frequent patterns from the transaction dataset");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("closed and maximal pattern mining");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);
        fpUtility.getConditionalDBs();
        List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();


        List<FrequentPatternRunner.TransactionItemExt> closed = fpUtility.getMaximalItems(frequentItems);

        Assert.assertNotNull(closed);

        String output = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: closed) {
            output = output + "\n" + itemExt;
        }

        Assert.assertEquals(
                "\n2 [frequent mining]\n" +
                        "2 [mining pattern]",output);

    }

    @Test
    public void getFrequentItems_test_allPermutations_Edge() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("D");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("E");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("F");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("G");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,1);
        List<FrequentPatternRunner.TransactionItemExt> frequentItems = fpUtility.getFrequentItems();

        Assert.assertNotNull(frequentItems);

        String output = "";
        for (FrequentPatternRunner.TransactionItemExt itemExt: frequentItems) {
            output = output + "\n" + itemExt;
        }

        Assert.assertEquals(
                "\n1 [B]\n" +
                        "1 [D]\n" +
                        "1 [E]\n" +
                        "1 [F]\n" +
                        "1 [G]",output);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_1() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("M O N K E Y");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("D O N K E Y");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("M A K E");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("M U C K Y");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("C O O K I E");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,3);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_2() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("f a c d g i m p");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("a b c f l m o");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("b f h j o");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("b c k s p");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("a f c e l p m n");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,3);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_3() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("1 2 5");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("2 4");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("2 3");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("1 2 4");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("1 3");
        FrequentPatternRunner.Transaction transaction6 = new FrequentPatternRunner.Transaction("2 3");
        FrequentPatternRunner.Transaction transaction7 = new FrequentPatternRunner.Transaction("1 3");
        FrequentPatternRunner.Transaction transaction8 = new FrequentPatternRunner.Transaction("1 2 3 5");
        FrequentPatternRunner.Transaction transaction9 = new FrequentPatternRunner.Transaction("1 2 3");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        transactions.add(transaction6);
        transactions.add(transaction7);
        transactions.add(transaction8);
        transactions.add(transaction9);
        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_4() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("b a c d g m p");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("a b c f l m o");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("b f h o");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("b k c p");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("a f c l p m n");

        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,3);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_5() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("f a c d g i m p");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("a b c f l m o");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("b f h j o w");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("b c k s p");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("a f c e l p m n");

        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,3);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_6() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("e a d b");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("d a c e b");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("c a b e");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("b a d");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("d");
        FrequentPatternRunner.Transaction transaction6 = new FrequentPatternRunner.Transaction("d b");
        FrequentPatternRunner.Transaction transaction7 = new FrequentPatternRunner.Transaction("a d e");
        FrequentPatternRunner.Transaction transaction8 = new FrequentPatternRunner.Transaction("b c");

        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        transactions.add(transaction6);
        transactions.add(transaction7);
        transactions.add(transaction8);
        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,3);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_7() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("a b");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("b c d");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("a c d e");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("a d e");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("a b c");
        FrequentPatternRunner.Transaction transaction6 = new FrequentPatternRunner.Transaction("a b c d");
        FrequentPatternRunner.Transaction transaction7 = new FrequentPatternRunner.Transaction("a");
        FrequentPatternRunner.Transaction transaction8 = new FrequentPatternRunner.Transaction("a b c");
        FrequentPatternRunner.Transaction transaction9 = new FrequentPatternRunner.Transaction("a b d");
        FrequentPatternRunner.Transaction transaction10 = new FrequentPatternRunner.Transaction("b c e");

        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        transactions.add(transaction6);
        transactions.add(transaction7);
        transactions.add(transaction8);
        transactions.add(transaction9);
        transactions.add(transaction10);
        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_8() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("1 3 4");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("2 3 5");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("1 2 3 5");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("2 5");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("1 2 3 5");

        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_9() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("a b c d");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("a d");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("a e");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("c e");

        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_10() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("a");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("a d");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("a e");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("c e");

        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);

        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_11() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("a b");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("b c d");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("a c d e");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("a d e");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("a b c");
        FrequentPatternRunner.Transaction transaction6 = new FrequentPatternRunner.Transaction("a b c d");
        FrequentPatternRunner.Transaction transaction7 = new FrequentPatternRunner.Transaction("a");
        FrequentPatternRunner.Transaction transaction8 = new FrequentPatternRunner.Transaction("a b c");
        FrequentPatternRunner.Transaction transaction9 = new FrequentPatternRunner.Transaction("a b d");
        FrequentPatternRunner.Transaction transaction10 = new FrequentPatternRunner.Transaction("b c e");


        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        transactions.add(transaction6);
        transactions.add(transaction7);
        transactions.add(transaction8);
        transactions.add(transaction9);
        transactions.add(transaction10);


        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_12() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("h a d b c");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("d a e f");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("c d b e");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("b a c d");
        FrequentPatternRunner.Transaction transaction5 = new FrequentPatternRunner.Transaction("b g c");


        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);


        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }

    @Test
    public void getFrequentItems_test_all_Permutations_Edge_13() {
        FrequentPatternRunner.Transaction transaction1 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction2 = new FrequentPatternRunner.Transaction("B A C E D");
        FrequentPatternRunner.Transaction transaction3 = new FrequentPatternRunner.Transaction("A C");
        FrequentPatternRunner.Transaction transaction4 = new FrequentPatternRunner.Transaction("C B D");
        List<FrequentPatternRunner.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
//        transactions.add(transaction3);
//        transactions.add(transaction4);


        FrequentPatternRunner.FPUtility fpUtility = new FrequentPatternRunner.FPUtility(transactions,2);

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


        String output = frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput;
        System.out.println(frequentItemsOutput.trim() + "\n" + closedItemsOutput + "\n" + maxItemsOutput);
    }


}


