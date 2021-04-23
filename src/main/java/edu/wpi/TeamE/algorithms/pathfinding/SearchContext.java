package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.algorithms.pathfinding.constraints.*;

public class SearchContext {
    private Searcher search;
    private CompositeConstraint constraints;

    public SearchContext(Searcher _search, String _type){
        search = _search;
        constraints = new CompositeConstraint();
        setConstraint(_type);
    }

    public SearchContext(){
        this("VANILLA");
    }

    public SearchContext(String _type){
        this(new Searcher(), _type);
    }

    public void setAlgo(Searcher newSearch){
        search = newSearch;
        search.setType(constraints);
    }

    public void setConstraint(String _type){
        SearchConstraint constraint = translateConstraint(_type);
        search.setType(constraint);
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

    public Path search(String startNode, String endNode){
        return search.search(startNode, endNode);
    }

    public Path searchAlongPath(Path route, String stopType){
        return search.searchAlongPath(route, stopType);
    }

    private SearchConstraint translateConstraint(String type){
        if(type.equalsIgnoreCase("SAFE")){
            return new SafeSearch();
        } else if(type.equalsIgnoreCase("HANDICAP")){
            return new HandicapSearch();
        } else if(type.equalsIgnoreCase("VANILLA")) {
            return new VanillaSearch();
        } else {
            return null;
        }
    }

    private Searcher translateAlgo(String algo){
        if(algo.equalsIgnoreCase("A*")){
            return new Searcher();
        } else if(algo.equalsIgnoreCase("DFS")){
            return new DFSSearcher();
        } else {
            return null;
        }
    }


    public void refresh(){
        search.refreshGraph();
    }

    public Node getNode(String id){
        return search.getNode(id);
    }
}
