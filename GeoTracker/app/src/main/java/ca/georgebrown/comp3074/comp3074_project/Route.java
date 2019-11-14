package ca.georgebrown.comp3074.comp3074_project;

public class Route {
    private String start,stop,via,difficulty,date, distance, duration;
    public static final Route[] routes = {
            new Route("1470 Queen Street West", "910 King Street West",
                    "Queen Street W and Sudbury Street",
                    "10 October, 2019", "Easy", "2.2 km", "27 min"),
            new Route("910 King Street West", "1470 Queen Street West",
                    "Sudbury Street and Queen Street W",
                    "11 October, 2019", "Easy", "2.2 km", "28 min"),
            new Route("2328 Sheppard Avenue West", "2500 Weston Road",
                    "Strathburn Blvd and Weston Rd",
                    "17 October, 2019", "Easy", "3.9 km", "48 min"),
            new Route("2328 Sheppard Avenue West", "2500 Weston Road",
                    "Humber River Recreational Trail",
                    "21 October, 2019", "Medium", "4.2 km", "50 min"),
    };
    public Route(String start, String stop, String via, String date, String difficulty, String distance, String duration) {
        this.date = date;
        this.start = start;
        this.stop = stop;
        this.via = via;
        this.difficulty = difficulty;
        this.distance = distance;
        this.duration = duration;
    }

    public String getStart() {
        return start;
    }

    public String getStop() {
        return stop;
    }

    public String getVia() {
        return via;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getDate() {
        return date;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }
}