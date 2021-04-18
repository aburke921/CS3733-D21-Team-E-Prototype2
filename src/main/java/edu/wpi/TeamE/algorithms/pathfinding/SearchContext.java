package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.algorithms.pathfinding.constraints.*;

public class SearchContext {
    private Searcher search;
    private SearchConstraint searchType;

    public SearchContext(){
        this(new Searcher(), "VANILLA");
    }

    public SearchContext(Searcher _search, String _type){
        search = _search;
        setConstraint(_type);
    }

    public void setAlgo(Searcher newSearch){
        search = newSearch;
        search.setType(searchType);
    }

    public void setConstraint(String _type){
        searchType = translate(_type);
        search.setType(searchType);
    }

    public Path search(String startNode, String endNode){
        return search.search(startNode, endNode);
    }

    private SearchConstraint translate(String type){
        if(type.equalsIgnoreCase("SAFE")){
            return new SafeSearch();
        } else if(type.equalsIgnoreCase("HANDICAP")){
            return new HandicapSearch();
        } else if(type.equalsIgnoreCase("VANILLA")){
            return new VanillaSearch();
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
