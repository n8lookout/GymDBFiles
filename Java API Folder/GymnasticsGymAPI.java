/**
 * CSS 475 Team Project
 * 
 * @team Hex Girls
 * @authors Anna Rivas, Nasheeta Lott, Christopher Long, Nathaniel Fincham, Noa
 *          Uritsky
 * @version 3/8/2024
 */

class GymnasticsGymAPI {

    /**
     * Metin Method
     * 
     * @author The Team
     *
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Gymnastics Gym Management System!");
        System.out.println("Type '?' or 'help' to list all available APIs.");
        System.out.println("Type 'exit' to quit the program.");
        System.out.println("");

        String cmd = "";
        while (!cmd.equalsIgnoreCase("exit")) {
            System.out.print("GymnasticsGymSystem-# ");
            cmd = System.console().readLine();

            String[] mainCmdArr = cmd.split(" ", 2);

            if (mainCmdArr.length > 1) {
                System.out.println("Providing more than one command not supported");
                System.out.println("");
                continue;
            }

            switch (mainCmdArr[0]) {

                // General commands
                case "?":
                case "help":
                    listOfApis();
                    break;
                case "exit":
                    // quitting
                    break;

                // Students
                case Students.cmdlistAllStudents:
                    Students.listAllStudents(mainCmdArr);
                    break;
                case Students.cmdlistAllStudentsByDiffLevel:
                    Students.listAllStudentsByDiffLevel(mainCmdArr);
                    break;
                case Students.cmdlistAllStudentsByStatus:
                    Students.listAllStudentsByStatus(mainCmdArr);
                    break;
                case Students.cmdlistAllClassesForAStudent:
                    Students.listAllClassesForAStudent(mainCmdArr);
                    break;
                case Students.cmdgetStudentInfo:
                    Students.getStudentInfo(mainCmdArr);
                    break;
                case Students.cmdgetStudentEmerContact:
                    Students.getStudentEmerContact(mainCmdArr);
                    break;
                case Students.cmdgetStudentDiffLevel:
                    Students.getStudentDiffLevel(mainCmdArr);
                    break;
                case Students.cmdgetStudent_userName:
                    Students.getStudent_userName(mainCmdArr);
                    break;
                case Students.cmdgetEmerContact_userName:
                    Students.getEmerContact_userName(mainCmdArr);
                    break;
                case Students.cmdupdateStudentDiffLevel:
                    Students.updateStudentDiffLevel(mainCmdArr);
                    break;
                case Students.cmdupdateStudentStatus:
                    Students.updateStudentStatus(mainCmdArr);
                    break;
                case Students.cmdupdateStudentEmerContact:
                    Students.updateStudentEmerContact(mainCmdArr);
                    break;
                case Students.cmdaddStudentToClass:
                    Students.addStudentToClass(mainCmdArr);
                    break;
                case Students.cmdAddNewStudent:
                    Students.addNewStudent(mainCmdArr);
                    break;

                // Coaches
                case Coaches.cmdaddNewCoachAvailability:
                    Coaches.addNewCoachAvailability(mainCmdArr);
                    break;
                case Coaches.cmdaddNewCoach:
                    Coaches.addNewCoach(mainCmdArr);
                    break;
                case Coaches.cmdlistAllCoaches:
                    Coaches.listAllCoaches(mainCmdArr);
                    break;
                case Coaches.cmdlistAllAvailCoachByDateRange:
                    Coaches.listAllAvailCoachByDateRange(mainCmdArr);
                    break;
                case Coaches.cmdgetCoachInfo:
                    Coaches.getCoachInfo(mainCmdArr);
                    break;
                case Coaches.cmdgetCoachAvail:
                    Coaches.getCoachAvail(mainCmdArr);
                    break;
                case Coaches.cmdgetCoach_userName:
                    Coaches.getCoach_userName(mainCmdArr);
                    break;
                case Coaches.cmdShowCoachSchedule:
                    Coaches.showCoachSchedule(mainCmdArr);
                    break;
                case Coaches.cmdchangeCoachSchedule:
                    Coaches.changeCoachSchedule(mainCmdArr);
                    break;
                // Classes
                case Classes.cmdaddNewClass:
                    Classes.addNewClass(mainCmdArr);
                    break;
                case Classes.cmdlistAllClassesbyMissingCoach:
                    Classes.listAllClassesbyMissingCoach(mainCmdArr);
                    break;
                case Classes.cmdlistAllClassesByDate:
                    Classes.listAllClassesByDate(mainCmdArr);
                    break;
                case Classes.cmdlistAllClassAttendees:
                    Classes.listAllClassAttendees(mainCmdArr);
                    break;
                case Classes.cmdlistClassByDifficultyLevel:
                    Classes.listClassByDifficultyLevel(mainCmdArr);
                    break;
                case Classes.cmdlistClassByEvent:
                    Classes.listClassByEvent(mainCmdArr);
                    break;
                case Classes.cmdlistClassByEventandDiffLevel:
                    Classes.listClassByEventandDiffLevel(mainCmdArr);
                    break;
                case Classes.cmdsendStatusNotification:
                    Classes.sendStatusNotification(mainCmdArr);
                    break;
                case Classes.cmdgetClassStatus:
                    Classes.getClassStatus(mainCmdArr);
                    break;
                case Classes.cmdgetClassEvent:
                    Classes.getClassEvent(mainCmdArr);
                    break;
                case Classes.cmdassignClassCoach:
                    Classes.assignClassCoach(mainCmdArr);
                case Classes.cmdupdateClassEvent:
                    Classes.updateClassEvent(mainCmdArr);
                    break;
                case Classes.cmdupdateClassStatus:
                    Classes.updateClassStatus(mainCmdArr);
                    break;
                case Classes.cmdupdateClassStartTime:
                    Classes.updateClassStartTime(mainCmdArr);
                    break;

                // Emergency Contacts
                case EmergencyContacts.cmdupdateEmergencyContact:
                    EmergencyContacts.updateEmergencyContact(mainCmdArr);
                    break;

                default:
                    System.out.println("Command not recognized, please try again");
            }
            System.out.println("");
        }

        GymnasticsGymDB.disconnect();
    }

    /**
     * List all available APIs
     * 
     * @author The Team
     *
     */
    private static void listOfApis() {
        Students.listAllStudents(null);
        Students.listAllStudentsByDiffLevel(null);
        Students.listAllStudentsByStatus(null);
        Students.listAllClassesForAStudent(null);
        Students.getStudentInfo(null);
        Students.getStudentEmerContact(null);
        Students.getStudentDiffLevel(null);
        Students.getStudent_userName(null);
        Students.getEmerContact_userName(null);
        Students.updateStudentDiffLevel(null);
        Students.updateStudentStatus(null);
        Students.updateStudentEmerContact(null);
        Students.addStudentToClass(null);
        Students.addNewStudent(null);


        Coaches.addNewCoach(null);
        Coaches.addNewCoachAvailability(null);
        Coaches.listAllCoaches(null);
        Coaches.listAllAvailCoachByDateRange(null);
        Coaches.getCoachInfo(null);
        Coaches.getCoachAvail(null);
        Coaches.getCoach_userName(null);
        Coaches.showCoachSchedule(null);
        Coaches.changeCoachSchedule(null);

        Classes.addNewClass(null);
        Classes.listAllClassesbyMissingCoach(null);
        Classes.listAllClassesByDate(null);
        Classes.listAllClassAttendees(null);
        Classes.listClassByDifficultyLevel(null);
        Classes.listClassByEvent(null);
        Classes.listClassByEventandDiffLevel(null);
        Classes.sendStatusNotification(null);
        Classes.getClassStatus(null);
        Classes.getClassEvent(null);
        // Classes.updateClassCoach(null);
        Classes.updateClassEvent(null);
        Classes.updateClassStatus(null);
        Classes.updateClassStartTime(null);
        Classes.assignClassCoach(null);

        EmergencyContacts.updateEmergencyContact(null);
    }
}
