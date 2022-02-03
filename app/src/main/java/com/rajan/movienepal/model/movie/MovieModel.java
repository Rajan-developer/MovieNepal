package com.rajan.movienepal.model.movie;

import java.util.ArrayList;

public class MovieModel {
    private Dates dates;
    private int page;
    private ArrayList<Result> results;

    public Dates getDates() {
        return dates;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Result> getResultList() {
        return results;
    }

    public void setResultList(ArrayList<Result> resultList) {
        this.results = resultList;
    }

    public class Dates {
        String maximun;
        String minimum;
    }


    public class Result {
        private int id;
        private String title;
        private String release_date;
        private double vote_average;
        private String overview;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public double getVote_average() {
            return vote_average;
        }

        public void setVote_average(double vote_average) {
            this.vote_average = vote_average;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }
    }
}
