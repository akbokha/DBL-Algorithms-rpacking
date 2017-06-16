import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by s157035 on 12-6-2017.
 */
public class PackerTester {
    private static long curTime;

    private static final int NUMBER_OF_TESTS = 1;
    private static final int STARTING_TEST = 4;
    private static final int NUMBER_OF_PERMUTATIONS = 1;
    private static final int NUMBER_OF_RECTANGLES = 10;

    private Random sortRandom = new Random();

    public static void main(String[] args) throws Exception {
        new PackerTester().runAllTests();
    }

    public void runAllTests() {
        curTime = System.currentTimeMillis(); // to check running time

        boolean graphical = true;

        ArrayList<TestRunTuple> tuples = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_TESTS; i++) {
            ADT_Rectangle[] rects = generateInput();
            
//            makeSingleTest(tuples, rects, true, i);
            //makeSingleTest(tuples, rects, false, i);
        }
        readTests(tuples);

        generateCSV(tuples, ".//test_sortOnDim_run_" + NUMBER_OF_RECTANGLES);

        long endTime = System.currentTimeMillis(); // end time
        System.err.println((endTime - curTime) + "ms");
    }
    
    private void readTests(ArrayList<TestRunTuple> tuples) {
        for(int i = STARTING_TEST; i < NUMBER_OF_TESTS + STARTING_TEST; i++) {
            String file = ".\\test\\custom\\10_0_hf_rn_min1_max10_" + Integer.toString(i) + ".txt";
//            file = ".\\test\\10_01_h11_rn.txt";
            
            Input_Scanner input = new Input_File(file);
            
            ADT_Area area = input.read();
            
            StrategyPicker.area = area;
            
            long time = System.currentTimeMillis();
            
            ADT_Area result = StrategyPicker.pickStrategy().compute();
            
            int endTime = (int)(System.currentTimeMillis() - time);
            
            TestRunTuple tuple = calculateTuple(i, 0, result, endTime);
            tuples.add(tuple);
            
            new Output_GraphicalOutput(result).draw();
            System.err.println("next");
        }
    }

    private void makeSingleTest(ArrayList<TestRunTuple> tuples, ADT_Rectangle[] rects, boolean allowRotation, int run) {
        ADT_Area area = new ADT_Area(ADT_Rectangle.INF, ADT_Rectangle.INF, allowRotation, rects);
        float bestFillRate = 0;
        ADT_Area bestArea = null;

        for(int p = 0; p < NUMBER_OF_PERMUTATIONS; p++) {
            Arrays.sort(area.getRectangles(), (o1, o2) -> sortRandom.nextBoolean() ? -1 : 1);
            long startTime = System.currentTimeMillis();
            ADT_Area result = runTest(area);

            float surface = result.getDimensions().x*result.getDimensions().y;
            float fillRate = (result.getTotalAreaRectangles() / surface) * 100;

            if(fillRate > bestFillRate) {
                bestFillRate = fillRate;
                bestArea = result;
            }

            long compTime = System.currentTimeMillis() - startTime;

            TestRunTuple tuple = calculateTuple(run, p, result, compTime);
            tuples.add(tuple);
        }
    }

    private void generateCSV(ArrayList<TestRunTuple> tuples, String path) {
        if(tuples.size() != 0) {
            String newFileName = path + ".csv";
            System.out.println(newFileName);
            File newFile = new File(newFileName);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

                writer.write("Test Number,Input Run,Permutation,Total Rectangle Area,Average Rectangle Area,Rectangle Area Variance,Average Rectangle Ratio,Rectangle Ratio Variance,Rotations allowed,Fill Rate,Computation Time (ms)");
                writer.newLine();

                for (int i = 0; i < tuples.size(); i++) {
                    TestRunTuple t = tuples.get(i);
                    String s = ",";
                    int rotAllowed = t.rotationsAllowed ? 1 : 0;
                    String out = i + s + t.run + s + t.permutation + s + t.totalRectangleArea + s + t.averageRectangleArea + s + t.areaVariance + s + t.averageRectangleRatio + s + t.aspectVariance + s + rotAllowed + s + t.resultFillRate + s + t.computationTime;
                    writer.write(out);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {

            }
        }
    }

    private ADT_Area runTest(ADT_Area area) {
        return new StrategyPicker(area).pickStrategy().compute();
    }

    private ADT_Rectangle[] generateInput() {
        ADT_Rectangle[] rectangles = new ADT_Rectangle[NUMBER_OF_RECTANGLES];
        Random rand = new Random();
        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i] = new ADT_Rectangle(rand.nextInt(100) + 1, rand.nextInt(100) + 1, ADT_Rectangle.NOTSET, ADT_Rectangle.NOTSET, true);
        }

        return rectangles;
    }

    private TestRunTuple calculateTuple(int run, int perm, ADT_Area result, long computationTime) {

        ADT_Rectangle[] shapes = result.getRectangles();

        int totalRectArea = 0;
        int processedRectangles = 0;
        int numberFlipped = 0;
        float totalAspect = 0;
        float totalAspectVariance = 0;

        for(ADT_Rectangle r : shapes) {
            totalRectArea += r.getWidth() * r.getHeight();

            if(r.getFlipped()) {
                numberFlipped++;
            }

            float w = r.getWidth();
            float h = r.getHeight();

            float aspect = w > h ? w / h : h / w;
            totalAspect += aspect;

            processedRectangles++;
        }

        float averageAspect = totalAspect / (float)NUMBER_OF_RECTANGLES;
        float averageArea = totalRectArea / (float)NUMBER_OF_RECTANGLES;

        float totalAreaVariance = 0;
        for(ADT_Rectangle r : shapes) {
            float area = r.getWidth() * r.getHeight();

            float w = r.getWidth();
            float h = r.getHeight();

            float aspect = w > h ? w / h : h / w;
            totalAspectVariance += (aspect - averageAspect) * (aspect - averageAspect);
            totalAreaVariance += (area - averageArea) * (area - averageArea);

            processedRectangles++;
        }

        float aspectVariance = totalAspectVariance / (float)NUMBER_OF_RECTANGLES;
        float areaVariance = totalAreaVariance / (float)NUMBER_OF_RECTANGLES;

        float surface = result.getDimensions().x*result.getDimensions().y;
        float fillRate = (result.getTotalAreaRectangles() / surface) * 100;

        return new TestRunTuple(run, perm, totalRectArea, averageArea, areaVariance, averageAspect, aspectVariance, result.canFlip(), fillRate ,computationTime);
    }

    private class TestRunTuple {
        public int run;
        public int permutation;
        public float aspectVariance;
        public float areaVariance;
        public int totalRectangleArea;
        public float averageRectangleArea;
        public float averageRectangleRatio;
        public boolean rotationsAllowed;
        public float resultFillRate;
        public long computationTime;

        public TestRunTuple(int run, int permutation, int totalRectangleArea, float averageRectangleArea, float areaVariance, float averageRectangleRatio, float aspectVariance, boolean rotationsAllowed, float resultFillRate, long computationTime) {
            this.run = run;
            this.permutation = permutation;
            this.totalRectangleArea = totalRectangleArea;
            this.averageRectangleArea = averageRectangleArea;
            this.areaVariance = areaVariance;
            this.averageRectangleRatio = averageRectangleRatio;
            this.aspectVariance = aspectVariance;
            this.rotationsAllowed = rotationsAllowed;
            this.resultFillRate = resultFillRate;
            this.computationTime = computationTime;
        }
    }
}
