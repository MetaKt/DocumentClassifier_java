# Document Classifier in Java
## เกี่ยวกับ Repos นี้
Repos นี้เป็นส่วนหนึ่งของโปรเจค ai คัดแยกเอกสาร โดยนำโมเดลที่เทรนแล้ว convolutional_model.h5 ด้วย keras เวอร์ชั่น 2 มาปรับใช้กับภาษา Java และ deeplearning4j

ส่วนที่สำคัญจะประกอบไปด้วยสองส่วนหลัก คือ resource Folder และ AiApplication.java 

## resource Folder
โฟลเดอร์นี้เป็นพื้นที่ไว้เก็บเอกสารเพื่อนำมาคัดแยกประเภท และตัวโมเดล convolutional_model.h5 
เอกสารในรูปแบบของ .pdf ที่ต้องการจะคัดแยกจะต้องนำมาใส่ใน โฟลเดอร์ data_for_test/pdf 
ส่วน โฟลเดอร์ data_for_test/converted_img จะเป็นพื้นที่ไว้เก็บภาพผลลัพธ์หลังจากโปรแกรม AiApplication.java ได้แปลงจาก .pdf เป็น .jpg เพื่อนำไปเตรียมเข้าโมเดล

## AiApplication Program
โปรแกรมนี้คือการนำโมเดล convolutional_model.h5 ที่เทรนด้วย python (3.11.9) และ keras 2 มาปรับใช้เพื่อแยกประเภทเอกสารจากโฟลเดอร์ data_for_test/pdf 

โปรแกรมนี้เขียนด้วย java (19.0.1) โดยใช้ deeplearning4j (1.0.0-M2) และสามารถรันได้ด้วยการใช้ IDE เช่น Intellij 


### Main arguments
เมื่อกดรัน โปรแกรมนี้จะเริ่มจากรับ arguments สามตัว ซึ่งคือ path ของโฟลเดอร์และโมเดล **จำเป็นต้องปรับใช้เอง**
- args[0] คือ path ของโฟลเดอร์ data_for_test/pdf
- args[1] คือ path ของโฟลเดอร์ data_for_test/converted_img
- args[2] คือ path ของโมเดล convolutional_model.h5

### classifyPdfsFolder
หลังจาก import โมเดลสำเร็จ จะเป็นการใช้ฟังก์ชัน classifyPdfsInFolder โดยฟังก์ชั่นนี้จะนำภาพจากในโฟลเดอร์ data_for_test/pdf มาแปลงให้เป็ยภาพ .jpg และนำไปเก็บที่โฟลเดอร์ data_for_test/converted_img 
เมื่อเสร็จแล้ว รูปเหล่านั้นจะถูกนำไปเตรียมพร้อมก่อนจะเข้าโมเดล โดย
- จะถูกนำไปแปลงให้เป็น array ที่มี shape เป็น 128x128x3
- ทำการ normalize ค่าต่างๆด้วยการหาร 255 (ค่า 255 คือค่าของ pixel สี และนำมาหารเพื่อไม่ให้ตัวเลขใหญ่เกินไปและอยู่ใน range 0-1 )
- ทำการจัดเรียงลำดับ array ให้ตรงตามที่โมเดลต้องการด้วยฟังก์ชั่น image.permutei()








