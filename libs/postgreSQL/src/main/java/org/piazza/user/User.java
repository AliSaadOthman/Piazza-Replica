//package org.piazza;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.GenerationType;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class User {
//    @Id
//    @SequenceGenerator(
//            name = "user_id_sequence",
//            sequenceName = "user_id_sequence"
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "user_id_sequence"
//    )
////    private Integer id;
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String password;
//    private String role;
//}

package org.piazza.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;

    public String getemail() {
        return email;
    }
    public String getpassword() {
        return password;
    }
    public String getfirstName() {
        return firstName;
    }
    public String getlastName() {
        return lastName;
    }
    public String getrole() {
        return role;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}