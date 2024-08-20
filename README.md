# Document Classifier in Java
## เกี่ยวกับ Repos นี้
Repos นี้เป็นส่วนหนึ่งของโปรเจค ai คัดแยกเอกสาร โดยนำโมเดลที่เทรนแล้ว convolutional_model.h5 ด้วย keras เวอร์ชั่น 2 มาปรับใช้กับภาษา Java และ tools deeplearning4j

ส่วนที่สำดัญจะประกอบไปด้วยสองส่วนหลัก คือ resource Folder และ AiApplication.java 

## resource Folder
โฟลเดอร์นี้เป็นพื้นไว้เก็บเอกสารเพื่อนำมาคัดแยกประเภท และตัวโมเดล convolutional_model.h5 
เอกสารในรูปแบบของ .pdf ที่ต้องการจะคัดแยกจะต้องนำมาใส่ใน โฟลเดอร์ data_for_test/pdf 
ส่วน โฟลเดอร์ data_for_test/converted_img จะเป็นพื้นที่ไว้เก็บภาพผลลัพธ์หลังจากโปรแกรม AiApplication.java ได้แปลงจาก .pdf เป็น .jpg 


## AiApplication Program
