# Document Classifier in Java
## เกี่ยวกับ Repos นี้
Repos นี้เป็นส่วนหนึ่งของโปรเจค ai คัดแยกเอกสาร โดยนำโมเดลที่เทรนแล้ว convolutional_model.h5 ด้วย keras เวอร์ชั่น 2 มาปรับใช้กับภาษา Java และ deeplearning4j

ส่วนที่สำคัญจะประกอบไปด้วยสองส่วนหลัก คือ resource Folder และ AiApplication.java 

## resource Folder
โฟลเดอร์นี้เป็นพื้นไว้เก็บเอกสารเพื่อนำมาคัดแยกประเภท และตัวโมเดล convolutional_model.h5 
เอกสารในรูปแบบของ .pdf ที่ต้องการจะคัดแยกจะต้องนำมาใส่ใน โฟลเดอร์ data_for_test/pdf 
ส่วน โฟลเดอร์ data_for_test/converted_img จะเป็นพื้นที่ไว้เก็บภาพผลลัพธ์หลังจากโปรแกรม AiApplication.java ได้แปลงจาก .pdf เป็น .jpg เพื่อนำไปเตรียมเข้าโมเดล

## AiApplication Program
โปรแกรมนี้คือการนำโมเดล convolutional_model.h5 ที่เทรนด้วย python (3.11.9) และ keras 2 มาปรับใช้เพื่อแยกประเภทเอกสารจากโฟลเดอร์ data_for_test/pdf 

โปรแกรมนี้เขียนด้วย java (19.0.1) โดยใช้ deeplearning4j (1.0.0-M2) และสามารถรันได้ด้วยการใช้ IDE เช่น Intellij 

เมื่อกดรัน โปรแกรมนี้จะเริ่มจาก รับ arguments สามตัว ซึ่งคือ path ของดฟลเดอร์และโมเดล
- args[0] คือ path ของโฟลเดอร์ data_for_test/pdf
- args[1] คือ path ของโฟลเดอร์ data_for_test/converted_img
- args[2] คือ path ของโมเดล convolutional_model.h5

**จำเป็นต้องปรับใช้เอง**





