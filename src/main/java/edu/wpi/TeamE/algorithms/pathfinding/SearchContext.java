package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.algorithms.pathfinding.constraints.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SearchContext {
    private Searcher search;
    private final CompositeConstraint constraints;
    private List<Path> searchHistory;

    public SearchContext(String _search, String _type){
        constraints = new CompositeConstraint();
        searchHistory = new ArrayList<>();
        setAlgo(_search);
        addConstraint(_type);
    }

    public SearchContext(String _type){
        this("A*", _type);
    }

    public SearchContext(){
        this("VANILLA");
    }

    public void setAlgo(String _algo){
        search = translateAlgo(_algo);
        search.setType(constraints);
    }

    public void setConstraint(String _type){
        constraints.clear();
        addConstraint(_type);
    }

    public void addConstraint(String _type){
        SearchConstraint constraint = translateConstraint(_type);
        constraints.add(constraint);
        search.setType(constraints);
    }

    public void removeConstraint(String _type){
        SearchConstraint constraint = translateConstraint(_type);
        constraints.remove(constraint);
        search.setType(constraint);
    }

    public Path search(Object startNode, Object endNode){
        return save(search.search(startNode, endNode));
    }

    public Path search(List stops){
        return save(search.search(stops));
    }

    public Path searchAlongPath(Path route, String stopType){
        return save(search.searchAlongPath(route, stopType));
    }

    public Node findNearest(Node location, String nearestType){
        return search.findNearest(location, nearestType);
    }

    private SearchConstraint translateConstraint(String type){
        if(type.equalsIgnoreCase("SAFE")){
            return new SafeSearch();
        } else if(type.equalsIgnoreCase("HANDICAP")){
            return new HandicapSearch();
        } else if(type.equalsIgnoreCase("VANILLA") || type.equalsIgnoreCase("")) {
            return new VanillaSearch();
        } else {
            return null;
        }
    }

    private Searcher translateAlgo(String algo){
        if(algo.equalsIgnoreCase("A*")){
            return new Searcher();
        } else if(algo.equalsIgnoreCase("DFS")){
            return new XFirstSearcher("DFS");
        } else if(algo.equalsIgnoreCase("BFS")){
            return new XFirstSearcher("BFS");
        } else {
            return null;
        }
    }

    private Path save(Path route){
        searchHistory.add(route);
        return route;
    }

    public List<Path> getSearchHistory(){
        return searchHistory;
    }

    public void refresh(){
        search.refreshGraph();
    }

    public Node getNode(String id){
        return search.getNode(id);
    }
}
