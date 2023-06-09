import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    private static RouteMap routeMap = new RouteMap();
    public static void main(String[] args)  {

        readFile("Sample.rtf"); // Read and parse the data with regex and print the data in a nice format

/*
    Use the function in the RouteMap class called getAllRoutes(start, end) to find route.
    getAllRoutes implements Djtrika's pathfinding algorithm with no heuristics.
 */
//
//        HashMap<Site, Site> foundRoute = routeMap.getAllRoutes(routeMap.getSite("Edwards"), routeMap.getSite("Oda"));
//        System.out.println("Found Route Size: " + foundRoute.size());
//        System.out.println("Walking Back:\n");
//
//        Site current = routeMap.getSite("Oda");
//        LinkedList<Site> path = new LinkedList<>();
//        while(current != routeMap.getSite("Edwards")){
//            path.push(current);
//            current = foundRoute.get(current);
//        }
//        path.push(routeMap.getSite("Edwards"));
//
//        for (Site site : path){
//            System.out.println(site);
//        }
    }

    /*
        Reads the input file from the resources folder. If you don't have a resources folder you can also
        simply place the input file next to wherever your built project is stashed. That way you don't have
        to worry about file paths. You'd access the file directly because it's within the same package like
        "Sample.rtf".

        Printing the data relies on each object having a toString method implemented. Feel free to
        grab the toString methods from Site, Route, Cargo, and Vehicle as they really do print all
        the information you would need to print.
     */
    public static void readFile(String fileName){
        File sample = null;
        String line = "";
        String[] splitLine = null;

        try {
            sample = new File(Main.class.getResource(fileName).toURI());

            Scanner scanner = new Scanner(sample);
            String header = "";

            while(scanner.hasNextLine()){
                line = scanner.nextLine();

                if (line.length() == 0){
                    continue;
                }

                if (line.charAt(0) == '#'){
                    header = line.substring(1);
                    System.out.println("Header Set: " + header);
                    scanner.nextLine();
                    continue;
                }

                if (header.equals("network")){
                    splitLine = line.trim().split("(\\s){1}");

                    int id = Integer.parseInt(splitLine[0]);
                    Site startSite = null;
                    if (routeMap.hasSite(splitLine[1])){
                        startSite = routeMap.getSite(splitLine[1]);
                    } else {
                        startSite = new Site(splitLine[1]);
                    }
                    Site endSite = new Site(splitLine[2]);
                    int cost = Integer.parseInt(splitLine[3]);
                    int time = Integer.parseInt(splitLine[4]);
                    RouteType type = RouteType.forString(splitLine[5]);

                    Route route = new Route(id, startSite, endSite, cost, time, type);
                    startSite.addRoutes(route);
                    routeMap.addSites(startSite);
                }

                if (header.equals("vehicles")){
                    splitLine = line.split("\\s+");
                    Vehicle vehicle = new Vehicle(
                            Vehicle.VehicleType.forString(splitLine[0]),
                            splitLine[1],
                            routeMap.getSite(splitLine[2])

                    );
                    routeMap.addVehicle(vehicle);
                }

                if (header.equals("cargo list")){
                    splitLine = line.split("(,\\s+)+");
                    Cargo cargo = new Cargo(
                            Integer.parseInt(splitLine[0]),
                            splitLine[1],
                            routeMap.getSite(splitLine[2]),
                            routeMap.getSite(splitLine[3]),
                            Integer.parseInt(splitLine[4])
                    );
                    routeMap.addCargo(cargo);
                }
            }
        } catch (FileNotFoundException | URISyntaxException | NumberFormatException e) {
            if (e instanceof FileNotFoundException){
                System.err.println("Couldn't find file at path:\n\t" + sample.getPath());
            }
            else if (e instanceof NumberFormatException){
                System.err.println("\nSplit Error" );
            }
            e.printStackTrace();
        }

        System.out.println(routeMap);
    }
}
