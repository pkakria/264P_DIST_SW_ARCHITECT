/**
 * @(#)RegisterStudentHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * "Register a student for a course" command event handler.
 * This class now listens to events  of type EV_REGISTER_STUDENT_CHECKED
 */
public class RegisterStudentHandler extends CommandEventHandler {

    /**
     * Construct "Register a student for a course" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    public RegisterStudentHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
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
        String registrationCheckingCode = objTokenizer.nextToken();
        String sSID     = objTokenizer.nextToken();
        String sCID     = objTokenizer.nextToken();
        String sSection = objTokenizer.nextToken();
        // Get the student and course records.
        Student objStudent = this.objDataBase.getStudentRecord(sSID);
        Course objCourse = this.objDataBase.getCourseRecord(sCID, sSection);

        if (registrationCheckingCode.equals("0")) {
            return "Invalid student ID";
        }
        if (registrationCheckingCode.equals("1")) {
            return "Invalid course ID or course section";
        }
        // Check if the given course conflicts with any of the courses the student has registered.
        if (registrationCheckingCode.equals("2")){
            return "Registration Conflicts";
        }
        //check if maximumCapacity in course exceeds to 3 students, with more than three- it announces warning
        ArrayList student_register = objCourse.getRegisteredStudents();
        if (student_register.size() >= Course.maximumCapacity){
            EventBus.announce(EventBus.EV_SHOW,"The Class is overbooked!");
        }

        // Request validated. Proceed to register.
        this.objDataBase.makeARegistration(sSID, sCID, sSection);
        return "Successful!";
    }
}