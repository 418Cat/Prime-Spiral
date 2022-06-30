import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;

public class PrimeSpiral {

    public static void main(String[] args) {
        //args : all optionnal, but to change one, all of them must be present as the program doesn't check for types, you'll get an error
        //(int)imageSize (0 or 1)withLine (0 or 1)withSmallCircle (int)circleRadius (int)lineSize
        primeImage pI = new primeImage((args.length > 0 ? Integer.parseInt(args[0]) : 500), (args.length > 3 ? Integer.parseInt(args[3]) : 8), (args.length > 4 ? Integer.parseInt(args[4]) : 20));
        System.out.println("Generating image..");
        pI.drawSpiral((args.length > 1 ? (args[1] == "1" ? true : false) : true), (args.length > 2 ? (args[2] == "1" ? true : false) : false));
        System.out.println("\nSaving image..");
        saveImage(pI.bf, "spiral.png");
    }

    public static void saveImage(BufferedImage image, String filename) {
        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException e) {
            System.out.println("Error saving image");
        }
    }

}

class primeImage {

    public BufferedImage bf;
    int size;
    private int qttyOfNbPerSide;
    private int circleSize = 1;
    private int lineSize = 3;
    private int circleColor = 255;
    private int lineColor = 255;
    
    primeImage(int size, int circleSize, int lineSize) {
        this.size = size;
        this.circleSize = circleSize;
        this.lineSize = lineSize;
        
        bf = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        this.qttyOfNbPerSide = size/(lineSize);
    }

    public void drawSpiral(Boolean withLine, Boolean withSmallCircle){

        int sizeLength = 1;
        int iOfLength = 0;

        int[] coords = {(int)size/2, (int)size/2};
        int angle = 0; //between 0 and 3, from west to north to east to south

        int maxNb = (int)Math.pow(qttyOfNbPerSide, 2);

        for(int i = 1; i < maxNb; i++) {

            String percent = String.valueOf(((float)(i+1)/(float)maxNb)*100);
            System.out.print("\r" + percent.substring(0, (percent.length() > 4 ? 4 : percent.length())) + "%");

            if(coords[0] >= size || coords[1] >= size || coords[0] < 0 || coords[1] < 0) return;

            if(isPrime(i)) {
                drawCircle(coords[0], coords[1], circleSize);
            } else if(withSmallCircle) {
                drawCircle(coords[0], coords[1], circleSize/3);
            }

            if(iOfLength == sizeLength) { //when the line ends, reset the counter and change the angle
                iOfLength = 0;
                angle = Math.floorMod(angle+1, 4);

                if(angle%2 == 0) { //add 1 to the size of a line each 2 turns
                    sizeLength++;
                }
            }

            switch(angle) { //move the coordinates depending on the angle
                case 0:
                    if(withLine) traceLine(coords[0], coords[1], coords[0] + lineSize, coords[1]);
                    coords[0]+= lineSize;
                    break;
                
                case 1:
                    if(withLine) traceLine(coords[0], coords[1], coords[0], coords[1] - lineSize);
                    coords[1]-= lineSize;
                    break;

                case 2:
                    if(withLine) traceLine(coords[0], coords[1], coords[0] - lineSize, coords[1]);
                    coords[0]-= lineSize;
                    break;

                case 3:
                    if(withLine) traceLine(coords[0], coords[1], coords[0], coords[1] + lineSize);
                    coords[1]+= lineSize;
                    break;
            }
            
            iOfLength++;

        }
    }

    private boolean isInBounds(int x, int y) {
        return(x >= 0 && y >= 0 && x < size && y < size);
    }

    private void drawCircle(int x, int y, int radius) {

        for(int angle = 1; angle < 360; angle++) { //angle in randian

            for(int j = 0; j <= radius; j++) { //make a circle for each pixel of the diameter, so make a full circle

                int[] coordsPoint = {(int)((float)j*Math.cos(angle)+x), (int)((float)j*Math.sin(angle)+y)};

                if(isInBounds(coordsPoint[0], coordsPoint[1])) {
                    bf.setRGB(coordsPoint[0], coordsPoint[1], circleColor);
                }

            }
        }
    }

    private void traceLine(int x1, int y1, int x2, int y2){

        int distance = (int)Math.sqrt(
            Math.pow(x2-x1, 2) + 
            Math.pow(y2-y1, 2)
        );

        int[] vector = {(x2-x1)/distance, (y2-y1)/distance};

        int[] coordsPoint = {x1 + vector[0], y1 + vector[1]};

        for(int i = 0; i < distance; i++){

            if(isInBounds(coordsPoint[0], coordsPoint[1])) {
                bf.setRGB(coordsPoint[0], coordsPoint[1], lineColor);
            }

            coordsPoint[0]+=vector[0];
            coordsPoint[1]+=vector[1];
        }
    }

    private boolean isPrime(int nb) {

        if(nb <= 1) return(false);

        for(int i = 2; i < nb; i++) {
            if(nb%i == 0) return(false);
        }
        return(true);
    }

}
