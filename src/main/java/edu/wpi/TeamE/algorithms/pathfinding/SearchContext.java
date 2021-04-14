package edu.wpi.TeamE.algorithms.pathfinding;

public class SearchContext {
    private Searcher searchAlgo;
    public SearchContext(){

    }
    public SearchContext(Searcher _searchAlgo){
        searchAlgo = _searchAlgo;
    }

    public void setAlgo(Searcher _searchAlgo){
        searchAlgo = _searchAlgo;
    }

    public Path search(String startNode, String endNode){
        return searchAlgo.search(startNode, endNode);
    }

}
