package com.fzu.facheck.entity.RollCall;

import java.util.List;

public class SearchClassInfo {
    public String code;
    private List<ResultClass> resultClasses;
    public static class ResultClass{
        private String ClassName;
        private String ClassTime;
        private boolean state;
        private String Classid;
        public String getClassName(){
            return ClassName;
        }
        public String getClassTime(){
            return ClassTime;
        }
        public boolean getState(){
            return state;
        }
        public String getClassid() { return Classid; }
    }
    public List<ResultClass> getResultClasses(){
        return resultClasses;
    }
}
