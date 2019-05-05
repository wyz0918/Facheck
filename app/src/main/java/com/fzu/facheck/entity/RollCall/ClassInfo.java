package com.fzu.facheck.entity.RollCall;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    public String code;
    private RecordSignInfo recordsInfo;
    public RecordSignInfo getRecordsInfo(){
        return recordsInfo;
    }
    public void RecordInstance(){
        recordsInfo=new RecordSignInfo();
    }
    public static class RecordSignInfo{
        private String classId;
        private List<RollCallResult.RecordInfoBean> classSignInInfo;
        public String getClassId(){
            return classId;
        }
        public List<RollCallResult.RecordInfoBean> getClassSignInInfo(){
            return classSignInInfo;
        }
        public static class Student{
            private String authenticationId;
            private String name;
            public Student(String id,String name){
                this.authenticationId=id;
                this.name=name;
            }
            public String getName(){
                return name;
            }
            public String getAuthenticationId(){
                return authenticationId;
            }
        }
        public static class Record{
            private String time;
            private String attendratio;
            public Record(String time,String attend){
                this.time=time;
                this.attendratio=attend;
            }
            public String getTime(){
                return time;
            }
            public String getAttendratio(){
                return attendratio;
            }
        }
        public List<Student> getStudents(){
            List<Student> students=new ArrayList<>();
            RollCallResult.RecordInfoBean recordInfoBean;
            if(classSignInInfo!=null&&classSignInInfo.size()>0){
                recordInfoBean=classSignInInfo.get(0);
                for(RollCallResult.RecordInfoBean.NotYetSignedInBean no:recordInfoBean.getNotYetSignedIn()){
                    Student student=new Student(no.getAuthenticationId(),no.getName());
                    students.add(student);
                }
                for(RollCallResult.RecordInfoBean.AlreadySignedInBean al:recordInfoBean.getAlreadySignedIn()){
                    Student student=new Student(al.getAuthenticationId(),al.getName());
                    students.add(student);
                }
                return students;
            }
            else
            return students;
        }
        public List<Record> getRecord(){
            List<Record> records=new ArrayList<>();
            if(classSignInInfo!=null&&classSignInInfo.size()>0){
                for(RollCallResult.RecordInfoBean re:classSignInInfo){
                    Record record=new Record(re.getTime(),re.getAlreadyNumber()+"/"+re.getTotalNumber());
                    records.add(record);
                }
                return records;
            }
            else
                return records;
        }
    }
}
