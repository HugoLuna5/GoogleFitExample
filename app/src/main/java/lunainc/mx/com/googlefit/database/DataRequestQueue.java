package lunainc.mx.com.googlefit.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lunainc.mx.com.googlefit.model.Workout;


public class DataRequestQueue {

    private List<WorkoutDataRequest> list = Collections.synchronizedList(new ArrayList());

    public boolean hasNext() {
        return list.size() > 0;
    }

    public void addRequest(WorkoutDataRequest wdr) {
        list.add(wdr);
    }

    public class WorkoutDataRequest {
        public String command;
        public Workout workout;
    }
}
