package com.example.ai;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.graph.vertex.impl.L2Vertex;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AiApplication {

    public static List<String> convertPdfToImages(String pdfPath, String outputFolder) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        File pdfFile = new File(pdfPath);
        PDDocument document = Loader.loadPDF(pdfFile);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        String baseName = pdfFile.getName().replace(".pdf", "");

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            String imagePath = Paths.get(outputFolder, baseName + "_converted_" + page + ".jpg").toString();
            ImageIO.write(bim, "jpg", new File(imagePath));
            imagePaths.add(imagePath);
        }
        document.close();
        return imagePaths;
    }

    public static INDArray preprocessImage(String imagePath) throws IOException {
        NativeImageLoader loader = new NativeImageLoader(128, 128, 3);
        INDArray image = loader.asMatrix(new File(imagePath));
        image.divi(255);

        return image;
    }

    public static void classifyPdfsInFolder(String pdfFolder, MultiLayerNetwork model, String outputFolder) throws IOException {
        File folder = new File(pdfFolder);
        for (File pdfFile : folder.listFiles()) {
            if (pdfFile.getName().endsWith(".pdf")) {
                String pdfPath = pdfFile.getAbsolutePath();
                List<String> imagePaths = convertPdfToImages(pdfPath, outputFolder);
//                System.out.println("Converted done");
                for (String imagePath : imagePaths) {
                    INDArray image = preprocessImage(imagePath);
//                    System.out.println("Process done");

                    image.permutei(0,2,3,1);
//                    System.out.println("Image shape" + image.shapeInfoToString());
                    INDArray output = model.output(image);
//                    System.out.println("Classifier done");
                    int predictedClass = output.argMax(1).getInt(0);
                    double confidence = output.getDouble(predictedClass) * 100;

                    Map<Integer, String> categoryLabels = new HashMap<>();
                    categoryLabels.put(0, "OR");
                    categoryLabels.put(1, "invoice");
                    categoryLabels.put(2, "payment");

                    String categoryLabel = categoryLabels.get(predictedClass);
                    System.out.printf("PDF: %s, Predicted Type: %s, Confidence: %.2f%%%n",
                            pdfFile.getName(), categoryLabel, confidence);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {

        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);

        String pdfFolder = args[0];
        String outputFolder = args[1];

        // Load the model
        File modelFile = new File(args[2]);

        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(String.valueOf(modelFile), false);
        classifyPdfsInFolder(pdfFolder, model, outputFolder);
    }
}
