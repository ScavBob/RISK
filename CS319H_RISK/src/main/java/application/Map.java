package application;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Map implements Serializable{
    private String mapName;
    private boolean [][] adjacenyMatrix;
    private Region[] regionList;
    private int mapRegionCount, regionCounter;
    private int continentCount;
    private int mapContinentCount;
    private int[] continentBonus;
    private int[] continentRegionNumbers;
    private String[] continentNames;

    public Map(String mapName, String mapPath) {
        this.mapName = mapName;
        initializeRegions(mapPath);
    }

    /**
     * Function intilailzes the regions and the instances of the map class by using the txt file.
     * @param mapPath takes the location of the file includes the map settings.
     */
    private void initializeRegions(String mapPath){

        Scanner scanner = null;
        InputStream is = getClass().getResourceAsStream(mapPath);
        scanner = new Scanner(is);

        continentCount = scanner.nextInt();
        mapContinentCount = continentCount;
        continentNames = new String[mapContinentCount];
        int totalRegionCount = scanner.nextInt();
        mapRegionCount = totalRegionCount;
        String regionName,continentName;
        regionList = new Region[totalRegionCount];
        regionCounter = 0;
        continentBonus = new int[continentCount];
        continentRegionNumbers = new int[continentCount];

        for (int i = 0; i < continentCount; i++){
            int continentID = scanner.nextInt();
            int regionCount = scanner.nextInt();
            continentRegionNumbers[i] = regionCount;
            continentBonus[i] = scanner.nextInt();
            continentNames[i] = scanner.next();

            for (int j = 0; j < regionCount; j++){
                int regionID = scanner.nextInt();
                int regionX = scanner.nextInt();
                int regionY = scanner.nextInt();
                regionName = scanner.next();
                regionName = regionName.replaceAll("_", " ");
                Region tmpRegion = new Region(regionID,regionName, regionX, regionY, continentID);
                regionList[regionCounter++]= tmpRegion;
            }
        }

        adjacenyMatrix = new boolean[totalRegionCount][totalRegionCount];
        for (int row = 0; row < totalRegionCount; row++) {
            for (int col = 0; col < totalRegionCount; col++) {
                adjacenyMatrix[row][col] = false;
            }
        }
        while (scanner.hasNextInt()){
            int firstRegion = scanner.nextInt();
            int secondRegion = scanner.nextInt();
            adjacenyMatrix[firstRegion][secondRegion] = true;
            adjacenyMatrix[secondRegion][firstRegion] = true;
        }
    }


    /**
     * Function checks the connection between regions.
     * @param r1 first region that the connection  going to check.
     * @param r2 second region that is going to check the connection.
     * @return boolean the condition of the connection of the two region.
     */
    public boolean isAdjacentRegion(Region r1, Region r2 ){
        return adjacenyMatrix[r1.RegionID()][r2.RegionID()];
    }


    /**
     * Get regionList method for the game logic.
     * @return the regionList of the map.
     */
    public Region[] getRegionList(){
        return regionList;
    }


    /**
     * Function updates the region according to the owners unit.
     * @param owner the player has the region.
     * @param region the target region is going to be checked.
     * @param newUnitCount last unit count of the region.
     */
    public void updateRegion(Region region, int newUnitCount, Player owner ){
        int location = -1;
        //location = java.util.Arrays.binarySearch(regionList,region);
        for(int i = 0; i < regionList.length;i++){
            if(regionList[i] == region)
                location = i;
        }
        regionList[location].setOwner(owner);
        regionList[location].setUnitCount(newUnitCount);

    }


    /**
     * Function gets the name of the map.
     * @return  name of the map.
     */
    public String getMapName(){
        return mapName;
    }


    /**
     * Function gets the bonus of the continent
     * @return int[]  bonus of the continent.
     */
    public int[] getContinentBonus(){
        return continentBonus;
    }

    /**
     * @return  int region count of the map.
     */
    public int getMapRegionCount() {
        return mapRegionCount;
    }

    /**
     * @return int[] count of the continent.
     */
    public int getMapContinentCount(){
        return mapContinentCount;
    }


    /**
     * @return  int [] continent region numbers.
     */
    public int[] getContinentRegionNumbers(){
        return continentRegionNumbers;
    }


    /**
     * @param r region that is the target region
     * @return ArrayList<Region> neighbour regions of the region parameter
     */
    public ArrayList<Region> getNeighbourOf(Region r){
        ArrayList<Region> list = new ArrayList<Region>();
        for(int i = 0; i< adjacenyMatrix[r.RegionID()].length;i++){
            if(adjacenyMatrix[r.RegionID()][i] )
                list.add(findByID(i));
        }
        return list;
    }


    /**
     * Function finds the Region according to the ID
     * @param id  gets the target id
     * @return  name of the map.
     */
    private Region findByID(int id){
        for(int i = 0;i < regionList.length;i++){
            if(regionList[i].RegionID() == id)
                return regionList[i];
        }
        return null;
    }

    /**
     * Function checks the is there a connection beetween two region.
     * @param source The source region is going to checked for a connection.
     * @param target  The target region is going to checked for a connection.
     * @return boolean returns the connection status as calling the isConnected method.
     */
    public boolean isConnected(Region source, Region target){

        boolean[] available = new boolean[adjacenyMatrix[0].length];
        for(int i = 0;i < available.length;i++){
            available[i] = true;
        }

        return isConnected(target,source,available);
    }

    /**
     * Function returns the connection status of two region
     * @param target target region is going to be checked for connection
     * @param available boolean array of the available regions.
     * @param current source region is going to be checked for connection
     * @return  the connection status.
     */
    private boolean isConnected(Region target,Region current, boolean[] available){
        if(current == target)
            return true;
        if(current.getOwner() != target.getOwner())
            return false;
        if(!available[current.RegionID()])
            return false;
        available[current.RegionID()] = false;
        ArrayList<Region> neighbours = getNeighbourOf(current);

        for(int i = 0;i< neighbours.size();i++){
            if(isConnected(target,neighbours.get(i),available)){
                return true;
            }
        }
        return false;
    }

    /**
     * @return String[] array of the continent names.
     */
    public String[] getContinentNames(){
        return continentNames;
    }

    /**
     * @return  gets the name of the continent.
     */
    public String getContinentName(int continentID){
        return continentNames[continentID];
    }
}
