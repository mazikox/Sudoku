package pl.mazurek.springboot.config;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Test {

    public static final String DATASETPATH = "C:\\Projects\\git\\backend\\springboot\\categories.arff";
    public static final String MODELPATH = "C:\\Projects\\git\\backend\\springboot\\model.bin";

    public static void main(String[] args) throws Exception {

        ModelGenerator mg = new ModelGenerator();

        Instances dataset = mg.loadDataset(DATASETPATH);

        int trainSize = (int) Math.round(dataset.numInstances() * 0.85);
        int testSize = dataset.numInstances() - trainSize;

        StringToWordVector filter = new StringToWordVector();


        String[] options = new String[]{"-R", "1"};
        filter.setOptions(options);

        filter.setInputFormat(dataset);
        filter.setLowerCaseTokens(true);

        Instances filteredData = Filter.useFilter(dataset, filter);


        Instances traindataset = new Instances(filteredData, 0, trainSize);
        Instances testdataset = new Instances(filteredData, trainSize, testSize);

        // build classifier with train dataset
//        Classifier ann =  mg.buildClassifier(traindataset);

        // Evaluate classifier with test dataset
//        System.out.println(mg.evaluateModel(ann, traindataset, testdataset));

        //Save model
//        mg.saveModel(ann, MODELPATH);

        ModelClassifier cls = new ModelClassifier();

        System.out.println("saved??????");
        String title = "rachunki";

        String className = cls.classifiy(Filter.useFilter(cls.createInstance(title, 0), filter), MODELPATH);
        System.out.println("\n The class name for the title '" + title + "' is " + className);


    }

    public static String genereteCategoryByTitle(String title) throws Exception {

        ModelGenerator mg = new ModelGenerator();
        Instances dataset = mg.loadDataset(DATASETPATH);

        StringToWordVector filter = new StringToWordVector();
        String[] options = new String[]{"-R", "1"};
        filter.setOptions(options);
        filter.setInputFormat(dataset);
        filter.setLowerCaseTokens(true);
        Filter.useFilter(dataset, filter);

        ModelClassifier cls = new ModelClassifier();

        return cls.classifiy(Filter.useFilter(cls.createInstance(title, 0), filter), MODELPATH);
    }
}