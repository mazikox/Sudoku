package pl.mazurek.springboot.weka;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class ModelClassifier {

    private Attribute title; //Update attribute from petalLength to title

    private ArrayList<Attribute> attributes;
    private ArrayList<String> classVal;
    private Instances dataRaw;


    public ModelClassifier() {
        title = new Attribute("title", (ArrayList)null); //Update attribute from petalLength to title and set as string type

        attributes = new ArrayList<Attribute>();
        classVal = new ArrayList<String>();
        classVal.add("zdrowie_i_uroda");
        classVal.add("zywnosc");
        classVal.add("transport");
        classVal.add("media");
        classVal.add("czynsz");
        attributes.add(title);
        attributes.add(new Attribute("class", classVal));
        dataRaw = new Instances("TestInstances", attributes, 0);
        dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
    }


    public Instances createInstance(String title, double result) {
        dataRaw.clear();
        double[] instanceValue1 = new double[]{dataRaw.attribute(0).addStringValue(title), 0};
        dataRaw.add(new DenseInstance(1.0, instanceValue1));
        return dataRaw;
    }


    public String classifiy(Instances insts, String path) {
        String result = "Not classified!!";
        Classifier cls = null;
        try {
            cls = (MultilayerPerceptron) SerializationHelper.read(path);
            result = classVal.get((int) cls.classifyInstance(insts.firstInstance()));
        } catch (Exception ex) {
            Logger.getLogger(ModelClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }


    public Instances getInstance() {
        return dataRaw;
    }


}