// SQL_db: cictems
// SQL_table: student_profile
// Mono Models
// Monosync Framewrok v9.08.16
// Generated using LazyMono
// This code is computer generated, do not modify
// Author: Jhon Melvin Nieto Perello
// Contact: jhmvinperello@gmail.com
//
// The Framework uses Hibernate as its ORM
// For more information about Hibernate visit hibernate.org

package app.lazy.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "student_profile", catalog = "cictems")
public class StudentProfileMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.Integer STUDENT_id;
private java.lang.Integer floor_assignment;
private java.lang.String profile_picture;
private java.lang.String student_address;
private java.lang.String zipcode;
private java.lang.String mobile;
private java.lang.String email;
private java.lang.String ice_name;
private java.lang.String ice_address;
private java.lang.String ice_contact;
private java.util.Date created_date;
private java.lang.Integer active;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "id", nullable = false, length = 10)
public java.lang.Integer getId() {
	return this.id;
}

public void setId(java.lang.Integer fieldId) {
	this.id = fieldId;
}

@Column(name = "STUDENT_id", nullable = true, length = 10)
public java.lang.Integer getSTUDENT_id() {
	return this.STUDENT_id;
}

public void setSTUDENT_id(java.lang.Integer fieldStudentId) {
	this.STUDENT_id = fieldStudentId;
}

@Column(name = "floor_assignment", nullable = true, length = 10)
public java.lang.Integer getFloor_assignment() {
	return this.floor_assignment;
}

public void setFloor_assignment(java.lang.Integer fieldFloorAssignment) {
	this.floor_assignment = fieldFloorAssignment;
}

@Column(name = "profile_picture", nullable = true, length = 60)
public java.lang.String getProfile_picture() {
	return this.profile_picture;
}

public void setProfile_picture(java.lang.String fieldProfilePicture) {
	this.profile_picture = fieldProfilePicture;
}

@Column(name = "student_address", nullable = true, length = 300)
public java.lang.String getStudent_address() {
	return this.student_address;
}

public void setStudent_address(java.lang.String fieldStudentAddress) {
	this.student_address = fieldStudentAddress;
}

@Column(name = "zipcode", nullable = true, length = 100)
public java.lang.String getZipcode() {
	return this.zipcode;
}

public void setZipcode(java.lang.String fieldZipcode) {
	this.zipcode = fieldZipcode;
}

@Column(name = "mobile", nullable = true, length = 50)
public java.lang.String getMobile() {
	return this.mobile;
}

public void setMobile(java.lang.String fieldMobile) {
	this.mobile = fieldMobile;
}

@Column(name = "email", nullable = true, length = 100)
public java.lang.String getEmail() {
	return this.email;
}

public void setEmail(java.lang.String fieldEmail) {
	this.email = fieldEmail;
}

@Column(name = "ice_name", nullable = true, length = 100)
public java.lang.String getIce_name() {
	return this.ice_name;
}

public void setIce_name(java.lang.String fieldIceName) {
	this.ice_name = fieldIceName;
}

@Column(name = "ice_address", nullable = true, length = 150)
public java.lang.String getIce_address() {
	return this.ice_address;
}

public void setIce_address(java.lang.String fieldIceAddress) {
	this.ice_address = fieldIceAddress;
}

@Column(name = "ice_contact", nullable = true, length = 50)
public java.lang.String getIce_contact() {
	return this.ice_contact;
}

public void setIce_contact(java.lang.String fieldIceContact) {
	this.ice_contact = fieldIceContact;
}

@Column(name = "created_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getCreated_date() {
	return this.created_date;
}

public void setCreated_date(java.util.Date fieldCreatedDate) {
	this.created_date = fieldCreatedDate;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public StudentProfileMapping copy() {
StudentProfileMapping copyMe = new StudentProfileMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.STUDENT_id = this.STUDENT_id;
copyMe.floor_assignment = this.floor_assignment;
copyMe.profile_picture = this.profile_picture;
copyMe.student_address = this.student_address;
copyMe.zipcode = this.zipcode;
copyMe.mobile = this.mobile;
copyMe.email = this.email;
copyMe.ice_name = this.ice_name;
copyMe.ice_address = this.ice_address;
copyMe.ice_contact = this.ice_contact;
copyMe.created_date = this.created_date;
copyMe.active = this.active;
return copyMe;
}

}
