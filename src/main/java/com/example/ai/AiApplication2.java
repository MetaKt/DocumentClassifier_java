package com.example.ai;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

import org.datavec.image.loader.ImageLoader;


import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.nd4j.linalg.factory.Nd4j;

import static org.deeplearning4j.nn.modelimport.keras.KerasModelImport.importKerasModelAndWeights;

public class AiApplication2 {
    private static final String MODEL_PATH = "C:\\Users\\meta2\\OneDrive\\Desktop\\NETbayJava\\ai\\ai\\src\\main\\java\\com\\example\\ai\\convolutional_model2.h5"; // Path to the trained model
//    private static String simpleMlp;
//
//    static {
//        try {
//            simpleMlp = new ClassPathResource("C:\\Users\\meta2\\OneDrive\\Desktop\\NETbayJava\\ai\\ai\\src\\main\\java\\com\\example\\ai\\convolutional_model.h5").getFile().getPath();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static final int IMG_WIDTH = 128;
    private static final int IMG_HEIGHT = 128;


    public static void main(String[] args) throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
        //Keras model saved was a sequential model
        //Use MultiLayerNetwork in Deeplearning4J when importing Sequential models

        //Load the weights and config (Computational?)
        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(MODEL_PATH, false);

        System.out.println("Import Model successful");

//        //pdf to img
//        convert_pdf_to_img();
//        System.out.println("Convert pdf to img successful");

        //Classifier (Model)
        classifier(model, IMG_WIDTH, IMG_HEIGHT, "C:\\Users\\meta2\\OneDrive\\Desktop\\NETbayJava\\ai\\ai\\src\\main\\java\\com\\example\\ai\\data_for_test\\converted_img");
        System.out.println("Classifier successful");

        // Prediction and Confidence

    }

    private static void classifier(MultiLayerNetwork model, int imageWidth, int imageHeight, String sourceDir) throws IOException {
        // Define the labels
        String[] labels = {"invoice", "OR", "payment"};

        // Get the directory
        File sourceFile = new File(sourceDir);
        // Get all files in the folder
        File[] listOfFiles = sourceFile.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

        // Check if the folder contains any files
        if (listOfFiles != null) {
            // Loop through all files
            for (File file : listOfFiles) {
                // Read each image
                BufferedImage img = ImageIO.read(file);

                // Debugging: Ensure the image is read correctly
                System.out.println("Processing image: " + file.getName());


                // Resize the image to the desired dimensions (128 x 128)
                BufferedImage resizedImg = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImg.createGraphics();
                g.drawImage(img, 0, 0, imageWidth, imageHeight, null);
                g.dispose();

                // Process the image
                ImageLoader loader = new ImageLoader(imageWidth, imageHeight, 3, true);
                System.out.println("Image dimensions: " + resizedImg.getWidth() + "x" + resizedImg.getHeight());
                INDArray input = loader.asMatrix(resizedImg);
                System.out.println("Resized Image" + resizedImg.getData());

                // Reshape input to add batch dimension
                input = input.reshape(1, imageWidth, imageHeight, 3);

                // Normalize the input by dividing by 255
                input.divi(255);

                // Debugging: Check the input to the model
                System.out.println("Input shape: " + input.shapeInfoToString());
                System.out.println("Input size: " + input.size(2));

                // Get the model's output for the image
                INDArray output = model.output(input);

                // Debugging: Check the raw model output
                System.out.println("Model output: " + output);

                // Find the index of the maximum value in the output array
                int predictedIndex = output.argMax(1).getInt(0);

                // Map the predicted index to the corresponding label
                String predictedLabel = labels[predictedIndex];

                // Print the predicted label
                System.out.println("Predicted Category for " + file.getName() + ": " + predictedLabel);
            }
        } else {
            System.out.println("No images found in the folder.");
        }
    }


    private static void convert_pdf_to_img() {
        try {
            String sourceDir = "C:\\Users\\meta2\\OneDrive\\Desktop\\NETbayJava\\ai\\ai\\src\\main\\java\\com\\example\\ai\\data_for_test\\pdf"; // Folder containing PDF files
            String destinationDir = "C:\\Users\\meta2\\OneDrive\\Desktop\\NETbayJava\\ai\\ai\\src\\main\\java\\com\\example\\ai\\data_for_test\\converted_img"; // Folder to save converted images

            File sourceFolder = new File(sourceDir);
            File destinationFolder = new File(destinationDir);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
                System.out.println("Folder Created -> " + destinationFolder.getAbsolutePath());
            }

            File[] listOfFiles = sourceFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
            if (listOfFiles != null) {
                for (File sourceFile : listOfFiles) {
                    if (sourceFile.isFile()) {
                        System.out.println("Processing file: " + sourceFile.getName());

                        PDDocument document = Loader.loadPDF(sourceFile);
                        PDFRenderer pdfRenderer = new PDFRenderer(document);

                        int numberOfPages = document.getNumberOfPages();
                        System.out.println("Total pages to be converted -> " + numberOfPages);

                        String fileName = sourceFile.getName().replace(".pdf", "");
                        String fileExtension = "jpg";
                        int dpi = 300; // Adjust dpi as needed

                        for (int i = 0; i < numberOfPages; ++i) {
                            File outPutFile = new File(destinationDir + File.separator + fileName + "_" + (i + 1) + "." + fileExtension);
                            BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                            ImageIO.write(bImage, fileExtension, outPutFile);
                        }

                        document.close();
                        System.out.println("Converted images are saved at -> " + destinationFolder.getAbsolutePath());
                    }
                }
            } else {
                System.err.println("No PDF files found in the folder: " + sourceFolder.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

