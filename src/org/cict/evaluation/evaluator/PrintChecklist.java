/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 *
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 *
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.evaluation.evaluator;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.cict.SubjectClassification;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;
import org.cict.reports.checklist.ACT;
import org.cict.reports.checklist.BITCT;
import org.cict.reports.checklist.BSIT1112;
import org.cict.reports.checklist.BSIT1516;

/**
 *
 * @author Joemar
 */
public class PrintChecklist extends Transaction  {

    public Integer CICT_id;
    
    private StudentMapping student;
    private String address = "", sectionName = "", highSchool = "";
    private CurriculumMapping curriculum;
    private ArrayList<Object[]> details = new ArrayList<>();
    private Integer CURRICULUM_ACT_id = 6,
            CURRICULUM_BSIT_new_id = 7, 
            CURRICULUM_BSIT_old_id = 9, 
            CURRICULUM_BITCT_id = 10;
    
    @Override
    protected boolean transaction() {
        student = Database.connect().student().getPrimary(CICT_id);
        if(student == null) {
            System.out.println("NO STUDENT");
            return false;
        }
        curriculum = Database.connect().curriculum().getPrimary( student.getCURRICULUM_id());
        if(curriculum == null) {
            System.out.println("NO CURRICULUM");
            return false;
        }
        if(student.getHas_profile() == 1) {
            StudentProfileMapping spMap = Mono.orm().newSearch(Database.connect().student_profile())
                    .eq(DB.student_profile().STUDENT_id, student.getCict_id())
                    .active().first();
            String hNum = spMap.getHouse_no(),
                    brgy = spMap.getBrgy(),
                    city = spMap.getCity(),
                    province = spMap.getProvince();
            if(hNum != null)
                address = hNum;
            if(brgy != null) {
                if(!address.isEmpty()) {
                    address += " " + spMap.getBrgy();
                } else
                    address = brgy;
            }
            if(city != null) {
                if(!address.isEmpty()) {
                    address += " " + city;
                } else
                    address = city;
            }
            if(province != null) {
                if(!address.isEmpty()) {
                    address += ", " + province;
                } else
                    address = province;
            }
            highSchool = "";
        }
        AcademicProgramMapping apMap = Database.connect().academic_program().getPrimary( curriculum.getACADPROG_id());
        sectionName = apMap.getCode() + " " + student.getYear_level() + student.getSection()
                    + "-G"+ student.get_group();
        
        CurricularLevelAssesor cla = new CurricularLevelAssesor(student);
        cla.assess();
        boolean prepData = cla.hasPrepData();
        CurriculumMapping primary = cla.getConsCurriculum();
        CurriculumMapping secondary = cla.getPrepCurriculum();
        
        if (cla.hasPrepData()) {
            String text = cla.getPrepCurriculum().getName() + " -> " + cla.getConsCurriculum().getName();
            System.out.println(text);
        } else {
            System.out.println(cla.getConsCurriculum().getName());
        }
        getResult(cla, 1, cla.hasPrepData());
        getResult(cla, 2, cla.hasPrepData());
        getResult(cla, 3, cla.hasPrepData());
        getResult(cla, 4, cla.hasPrepData());
        return true;
    }
    
    private void getResult(CurricularLevelAssesor cla, int year, boolean hasPrepData) {
        AssessmentResults result = cla.getAnnualAssessment(year);
        if(hasPrepData) {
            if(year<=2)
                curriculum = cla.getPrepCurriculum();
            else
                curriculum = cla.getConsCurriculum();
        } 
        
        for (int semester = 1; semester <= 2; semester++) {
            ArrayList<SubjectAssessmentDetials> sadetails;
            try {
                sadetails = result.getSemestralResults(semester);
            } catch (Exception e) {
                continue;
            }
            for(SubjectAssessmentDetials sadetail: sadetails) {
                SubjectMapping subject = sadetail.getSubjectDetails();
                Object[] detail = new Object[5];
                if(subject != null) {
                    detail[0] = subject; //code
                    String hrs = "";
                    if(subject.getLab_units() != 0.0) {
                        hrs = "5"; //hrs per wk;
                    } else if(subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_PE)) {
                        hrs = "2"; //hrs per wk;
                    } else if(subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_NSTP)) {
                        hrs = "";
                    } else
                        hrs = "3"; //hrs per wk;
                    detail[1] = hrs;
                    
                    String prereq = "";
                    ArrayList<CurriculumRequisiteLineMapping> crlMaps = sadetail.getSubjectRequisites();
                    if(crlMaps != null) {
                        for(CurriculumRequisiteLineMapping crlMap: crlMaps) {
                            SubjectMapping subjectPrereq = Database.connect().subject().getPrimary(crlMap.getSUBJECT_id_req());
                            if(prereq.isEmpty())
                                prereq = subjectPrereq.getCode();
                            else
                                prereq += ", " + subjectPrereq.getCode();
                        }
                    } else
                        prereq = "NONE";
                    detail[2] = prereq; 

                    String coreq = "";//coreq
                    ArrayList<CurriculumRequisiteExtMapping> creMaps = Mono.orm().newSearch(Database.connect().curriculum_requisite_ext())
                            .eq(DB.curriculum_requisite_ext().CURRICULUM_id, curriculum.getId())
                            .eq(DB.curriculum_requisite_ext().SUBJECT_id_get, subject.getId())
                            .eq(DB.curriculum_requisite_ext().type, "COREQUISITE")
                            .active()
                            .all();
                    if(creMaps != null) {
                        for(CurriculumRequisiteExtMapping creMap: creMaps) {
                            SubjectMapping subjectCoreq = Database.connect().subject().getPrimary(creMap.getSUBJECT_id_req());
                            if(coreq.isEmpty())
                                coreq = subjectCoreq.getCode();
                            else
                                coreq += ", " + subjectCoreq.getCode();
                        }
                    } else
                        coreq = "NONE";
                    detail[3] = coreq;
                    detail[4] = year + "" + semester;
                    details.add(detail);
                }
            }
        }
    }

    private ArrayList<Object[]> fyrfsem = new ArrayList<>();
    private ArrayList<Object[]> fyrssem = new ArrayList<>();
    private ArrayList<Object[]> syrfsem = new ArrayList<>();
    private ArrayList<Object[]> syrssem = new ArrayList<>();
    private ArrayList<Object[]> tyrfsem = new ArrayList<>();
    private ArrayList<Object[]> tyrssem = new ArrayList<>();
    private ArrayList<Object[]> fryrfsem = new ArrayList<>();
    private ArrayList<Object[]> fryrssem = new ArrayList<>();
    @Override
    protected void after() {
        if(curriculum.getId() == CURRICULUM_BSIT_new_id) { //temp
            BSIT1516 bsit1516 = new BSIT1516(student.getId() + "_"
                + Mono.orm()
                        .getServerTime()
                        .getCalendar()
                        .getTimeInMillis());
            bsit1516.STUDENT_NUMBER = student.getId();
            String fullName = student.getLast_name() + ", " + student.getFirst_name();
            String midName = student.getMiddle_name();
            if(midName != null)
                fullName += " " + midName;
            bsit1516.STUDENT_NAME = fullName;
            bsit1516.STUDENT_ADDRESS = address;
            for (Object[] detail: details) {
                String key = (String) detail[4];
                SubjectMapping subject = (SubjectMapping) detail[0];
                if(subject.getCode().equalsIgnoreCase("CAPS 01")) {
                    detail[2] = "Regular 3rd year \nStanding";
                    detail[3] = "Regular 3rd year \nStanding";
                }
                if(subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                    detail[1] = "486 hours";
                    detail[2] = "4th Year Standing";
                }
                switch(key) {
                    case "11":
                        store(fyrfsem, detail);
                        break;
                    case "12":
                        store(fyrssem, detail);
                        break;
                    case "21":
                        store(syrfsem, detail);
                        break;
                    case "22":
                        store(syrssem, detail);
                        break;
                    case "31":
                        store(tyrfsem, detail);
                        break;
                    case "32":
                        store(tyrssem, detail);
                        break;
                    case "41":
                        store(fryrfsem, detail);
                        break;
                    case "42":
                        store(fryrssem, detail);
                        break;
                }
                
            }
            bsit1516.SUBJECTS_PER_SEM.put("11", fyrfsem);
            bsit1516.SUBJECTS_PER_SEM.put("12", fyrssem);
            bsit1516.SUBJECTS_PER_SEM.put("21", syrfsem);
            bsit1516.SUBJECTS_PER_SEM.put("22", syrssem);
            bsit1516.SUBJECTS_PER_SEM.put("31", tyrfsem);
            bsit1516.SUBJECTS_PER_SEM.put("32", tyrssem);
            bsit1516.SUBJECTS_PER_SEM.put("41", fryrfsem);
            bsit1516.SUBJECTS_PER_SEM.put("42", fryrssem);
            int val = bsit1516.print();
        } else if(curriculum.getId() == CURRICULUM_ACT_id) {
            ACT act = new ACT(student.getId() + "_"
                + Mono.orm()
                        .getServerTime()
                        .getCalendar()
                        .getTimeInMillis());
            act.STUDENT_NUMBER = student.getId();
            String fullName = student.getLast_name() + ", " + student.getFirst_name();
            String midName = student.getMiddle_name();
            if(midName != null)
                fullName += " " + midName;
            act.STUDENT_NAME = fullName;
            act.STUDENT_COURSE_YR_SEC_GRP = sectionName;
            for (Object[] detail: details) {
                String key = (String) detail[4];
                SubjectMapping subject = (SubjectMapping) detail[0];
                switch(key) {
                    case "11":
                        store(fyrfsem, detail);
                        break;
                    case "12":
                        store(fyrssem, detail);
                        break;
                    case "21":
                        store(syrfsem, detail);
                        break;
                    case "22":
                        store(syrssem, detail);
                        break;
                }
            }
            act.SUBJECTS_PER_SEM.put("11", fyrfsem);
            act.SUBJECTS_PER_SEM.put("12", fyrssem);
            act.SUBJECTS_PER_SEM.put("21", syrfsem);
            act.SUBJECTS_PER_SEM.put("22", syrssem);
            int val = act.print();
        } else if(curriculum.getId() == CURRICULUM_BSIT_old_id) {
            BSIT1112 bsit1112 = new BSIT1112(student.getId() + "_"
                + Mono.orm()
                        .getServerTime()
                        .getCalendar()
                        .getTimeInMillis());
            bsit1112.STUDENT_NUMBER = student.getId();
            String fullName = student.getLast_name() + ", " + student.getFirst_name();
            String midName = student.getMiddle_name();
            if(midName != null)
                fullName += " " + midName;
            bsit1112.STUDENT_NAME = fullName;
            bsit1112.STUDENT_ADDRESS = address;
            bsit1112.STUDENT_HS = highSchool;
            bsit1112.IMAGE_LOCATION = "src/org/cict/reports/checklist/images/me.png";

            for (Object[] detail: details) {
                String key = (String) detail[4];
                SubjectMapping subject = (SubjectMapping) detail[0];
                switch(key) {
                    case "11":
                        store(fyrfsem, detail);
                        break;
                    case "12":
                        store(fyrssem, detail);
                        break;
                    case "21":
                        store(syrfsem, detail);
                        break;
                    case "22":
                        store(syrssem, detail);
                        break;
                    case "31":
                        store(tyrfsem, detail);
                        break;
                    case "32":
                        store(tyrssem, detail);
                        break;
                    case "41":
                        store(fryrfsem, detail);
                        break;
                    case "42":
                        store(fryrssem, detail);
                        break;
                }
                
            }
            bsit1112.SUBJECTS_PER_SEM.put("11", fyrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("12", fyrssem);
            bsit1112.SUBJECTS_PER_SEM.put("21", syrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("22", syrssem);
            bsit1112.SUBJECTS_PER_SEM.put("31", tyrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("32", tyrssem);
            bsit1112.SUBJECTS_PER_SEM.put("41", fryrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("42", fryrssem);
            bsit1112.print();
        } else if(curriculum.getId() == CURRICULUM_BITCT_id) {
            BITCT bsit1112 = new BITCT(student.getId() + "_"
                + Mono.orm()
                        .getServerTime()
                        .getCalendar()
                        .getTimeInMillis());
            bsit1112.STUDENT_NUMBER = student.getId();
            String fullName = student.getLast_name() + ", " + student.getFirst_name();
            String midName = student.getMiddle_name();
            if(midName != null)
                fullName += " " + midName;
            bsit1112.STUDENT_NAME = fullName;
            bsit1112.STUDENT_ADDRESS = address;
            bsit1112.STUDENT_HS = highSchool;
            bsit1112.IMAGE_LOCATION = "src/org/cict/reports/checklist/images/me.png";

            for (Object[] detail: details) {
                String key = (String) detail[4];
                SubjectMapping subject = (SubjectMapping) detail[0];
                switch(key) {
                    case "11":
                        store(fyrfsem, detail);
                        break;
                    case "12":
                        store(fyrssem, detail);
                        break;
                    case "21":
                        store(syrfsem, detail);
                        break;
                    case "22":
                        store(syrssem, detail);
                        break;
                    case "31":
                        store(tyrfsem, detail);
                        break;
                    case "32":
                        store(tyrssem, detail);
                        break;
                    case "41":
                        store(fryrfsem, detail);
                        break;
                    case "42":
                        store(fryrssem, detail);
                        break;
                }
                
            }
            bsit1112.SUBJECTS_PER_SEM.put("11", fyrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("12", fyrssem);
            bsit1112.SUBJECTS_PER_SEM.put("21", syrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("22", syrssem);
            bsit1112.SUBJECTS_PER_SEM.put("31", tyrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("32", tyrssem);
            bsit1112.SUBJECTS_PER_SEM.put("41", fryrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("42", fryrssem);
            bsit1112.print();
        }
    }
    
    private void store(ArrayList<Object[]> subjectDetails, Object[] detail) {
        subjectDetails.add(detail); 
    }
}