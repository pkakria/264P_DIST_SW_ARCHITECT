/**
 * @(#)RegisterStudentHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * "Conflict checker for a course" command event handler.
 * This module receives the register_student event, checks for null student and null subject
 * It checks if a conflict exists. If any anomaly exists, it announces an EV_SHOW message with
 * the appropriate message. If all checks pass, it sends an EV_REGISTER_STUDENT_CHECKED to the
 * event bus which is received by the register student handler. That handler then succesfully register
 * the student and pushes out a notification if more than 3 students are registered
 */
public class RegisterationConflictHandler extends CommandEventHandler {

    /**
     * Construct "Register a student for a course" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    public RegisterationConflictHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
        super(objDataBase, iCommandEvCode, iOutputEvCode);
    }

    /**
     * Process "Register a student for a course" command event.
     *
     * @param param a string parameter for command
     * @return a string result of command processing
     */
    protected String execute(String param) {
        // Parse the parameters.
        StringTokenizer objTokenizer = new StringTokenizer(param);
        String sSID     = objTokenizer.nextToken();
        String sCID     = objTokenizer.nextToken();
        String sSection = objTokenizer.nextToken();
        //output code for the RegisterStudentHandler to interpret
        // 0 -> objStudent ==null
        // 1 -> objCourse == null
        // 2 -> conflict found
        // 3 -> All checks passed
        String outputCode;

        // Get the student and course records.
        Student objStudent = this.objDataBase.getStudentRecord(sSID);
        Course objCourse = this.objDataBase.getCourseRecord(sCID, sSection);
        if (objStudent == null) {
            outputCode = "0";
            return (outputCode + " "+ sSID + " " + sCID + " " + sSection);
        }
        if (objCourse == null) {
            outputCode = "1";
            return (outputCode + " "+ sSID + " " + sCID + " " + sSection);
        }
        // Check if the given course conflicts with any of the courses the student has registered.
        boolean conflicts = false;
        ArrayList vCourse = objStudent.getRegisteredCourses();
        for (int i=0; i<vCourse.size(); i++) {
            if (((Course) vCourse.get(i)).conflicts(objCourse)) {
                conflicts = true;
            }
        }
        if (conflicts){
            outputCode = "2";
            return (outputCode + " "+ sSID + " " + sCID + " " + sSection);
        }
        outputCode = "3";
        // all looks good. announce an event for the registerStudentHAndler to take over from here
        return (outputCode + " "+ sSID + " " + sCID + " " + sSection);
    }
}