# Document Classifier in Java


## เกี่ยวกับ Repos นี้
Repos นี้เป็นส่วนหนึ่งของโปรเจค ai คัดแยกเอกสาร โดยนำโมเดลที่เทรนแล้ว .h5 ด้วย keras เวอร์ชั่น 2 มาปรับใช้กับภาษา Java และ deeplearning4j

ส่วนที่สำคัญจะประกอบไปด้วยสองส่วนหลัก คือ resource Folder และ AiApplication.java 

## resource Folder
โฟลเดอร์นี้จะอยู่ในส่วนของ gitignore ซึ่ง**จำเป็นต้องสร้างเอง** 
โฟลเดอร์นี้เป็นพื้นที่ไว้เก็บเอกสารเพื่อนำมาคัดแยกประเภท และตัวโมเดล .h5 โดยการสร้าง ในโฟลเดอร์นี้จำเป็นต้องมี
 - ตัวโมเดล .h5ที่ต้องการจะใช้
 - โฟลเดอร์ย่อย data_for_test ที่มีอีกสองโฟลเดอร์ย่อยข้างใน
     - pdf และ
     - converted_img
       
เอกสารในรูปแบบของ .pdf ที่ต้องการจะคัดแยกจะต้องนำมาใส่ใน โฟลเดอร์ data_for_test/pdf 
ส่วน โฟลเดอร์ data_for_test/converted_img จะเป็นพื้นที่ไว้เก็บภาพผลลัพธ์หลังจากโปรแกรม AiApplication.java ได้แปลงจาก .pdf เป็น .jpg เพื่อเตรียมนำเข้าโมเดล

## AiApplication Program
โปรแกรมนี้คือการนำโมเดล .h5 ที่เทรนด้วย python (3.11.9) และ keras 2 มาปรับใช้เพื่อแยกประเภทเอกสารจากโฟลเดอร์ data_for_test/pdf 

โปรแกรมนี้เขียนด้วย java (19.0.1) โดยใช้ deeplearning4j (1.0.0-M2) และสามารถรันได้ด้วยการใช้ IDE เช่น Intellij 

โดยผลลัพธ์ print ออกมาในรูปแบบ "PDF: %s, Predicted Type: %s, Confidence: %.2f%%%n"


### Main arguments
เมื่อกดรัน โปรแกรมนี้จะเริ่มจากรับ arguments สามตัว ซึ่งคือ path ของโฟลเดอร์และโมเดล **จำเป็นต้องปรับใช้เอง**
- args[0] คือ path ของโฟลเดอร์ data_for_test/pdf
- args[1] คือ path ของโฟลเดอร์ data_for_test/converted_img
- args[2] คือ path ของโมเดล model.h5

### classifyPdfsInFolder(String pdfFolder, MultiLayerNetwork model, String outputFolder)
หลังจาก import โมเดลสำเร็จ จะเป็นการใช้ฟังก์ชัน classifyPdfsInFolder 
- โดยอันดับแรกคือการแปลงไฟล์จาก .pdf เป็น .jpg
- จากนั้นนำไปเตรียมพร้อมเพื่อนำเข้าไปในโมเดล
- นำ data เข้าโมเดล
- นำผลลัพธ์มาคำนวณค่า confidence
  
Parameters:
- String pdfFolder : path ของโฟลเดอร์ที่มีไฟล์ pdf ที่ต้องการจะคัดแยก
- MultiLayerNetwork model : ตัวโมเดลที่ import เข้ามา
- String outputFolder : path ของโฟลเดอร์ที่มีไว้เก็บภาพ .jpg ที่แปลงมาจากไฟล์ .pdf 

#### convertPdfToImages(String pdfPath, String outputFolder)
  - โดยฟังก์ชั่นนี้จะนำภาพจากในโฟลเดอร์ data_for_test/pdf มาแปลงเป็นภาพ .jpg และนำไปเก็บที่โฟลเดอร์ data_for_test/converted_img 
เมื่อเสร็จแล้ว รูปเหล่านั้นจะถูกนำไปเตรียมพร้อมก่อนจะเข้าโมเดล โดยฟังก์ชั่น preprocessImage() และ image.permutei()

Parameters:
- String pdfPath : path ของไฟล์ .pdf เดี่ยวๆที่ loop ออกมาจาก pdfFolder
- String outputFolder : path ของโฟลเดอร์ที่มีไว้เก็บภาพ .jpg ที่แปลงมาจากไฟล์ .pdf


#### preprocessImage(String imagePath)
  - จะถูกนำไปแปลงให้เป็น array ที่มี shape เป็น 128x128x3
  - ทำการ normalize ค่าต่างๆด้วยการหาร 255 (ค่า 255 คือค่าของ pixel สี และนำมาหารเพื่อไม่ให้ตัวเลขใหญ่เกินไปและอยู่ใน range 0-1 )

Parameter:
- String imagePath : path ของรูปภาพ .jpg เดี่ยวๆที่จะนำไปแปลงใส่ array ก่อนจะนำเข้าโมเดล


#### image.permutei(int... var1);
  - ทำการจัดเรียงลำดับ array ให้ตรงตามที่โมเดลต้องการด้วยฟังก์ชั่น image.permutei()

Parameter :
- int... var1 : ลำดับมิติของ array ที่ต้องการจะจัดเรียงใหม่

#### model.output(INDArray input)
  - ฟังก์ชั่นนี้จะคำนวณผลลัพธ์ออกมาในรูปแบบของ one hot
  - โดยค่าผลลัพธ์นี้จะนำมาแสดง และคำนวณความน่าจะเป็น

Parameter :
- INDArray input : array ที่แปลงมาจากรูปภาพ และตรงตามมิติที่โมเดลต้องการ [1,128,128,3]

#### Map<Integer, String> categoryLabels 
  - Hash Map นี้จะจับคู่ประเภทเอกสารที่เราต้องจะจำแนกกับเลข index
  - **หากมีการเพิ่มเติมประเภทเอกสาร จำเป็นที่ตัวเลขจะต้องเรียงตามลำดับของโฟลเดอร์ประเภทเอกสารใน dataset_for_train เมื่อตอนเทรนด้วย python**


  








