package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Course;
import org.piazza.model.document.User;

import java.util.Date;


    @Edge("userBannedFromCourseEdge")
    public class UserBannedFromCourse extends BaseModel {

        @From(lazy = true)
        User user;
        @To(lazy = true)
        Course course;

        Date unbanDate;

        boolean isPermenant;

        public UserBannedFromCourse(User user, Course course, Date unbanDate, boolean isPermenant) {
            this.user=user;
            this.course = course;
            if(isPermenant){
                this.isPermenant = true;
            }
            else {
                this.unbanDate = unbanDate;
                this.isPermenant = false;
            }
        }
//        public UserBannedFromCourse(User user,Course course){ // use this constructor when the ban is permanant
//            this(user, course, null);
//            this.isPermenant=true;
////            this.user=user;
////            this.course=course;
////            this.isPermenant=true;
//        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public Date getUnbanDate() {
            return unbanDate;
        }

        public void setUnbanDate(Date unbanDate) {
            this.unbanDate = unbanDate;
        }

        public boolean isPermenant() {
            return isPermenant;
        }

        public void setPermenant(boolean permenant) {
            isPermenant = permenant;
        }

        @Override
        public String toString() {
            return "UserBannedFromCourse{" +
                    "user=" + user +
                    ", course=" + course +
                    ", unbanDate=" + unbanDate +
                    ", isPermenant=" + isPermenant +
                    '}';
        }
    }
