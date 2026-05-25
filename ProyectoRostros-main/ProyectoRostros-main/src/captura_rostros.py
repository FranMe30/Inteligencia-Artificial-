import cv2
import os
import time

dataset_dir = '../data/02_processed'
nombre_persona = 'Alumno_Francisco'
ruta_completa = os.path.join(dataset_dir, nombre_persona)
fotos_maximas = 300

os.makedirs(ruta_completa, exist_ok=True)

#Detector facial Haar Cascade
#Modelo preentrenado para detectar rostros frontales
face_cascade = cv2.CascadeClassifier(
    cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'
)
#abrir la camara web
cap = cv2.VideoCapture(0)

contador = 0
ultimo_guardado = 0
intervalo = 0.3  # segundos entre capturas

print(f"Iniciando captura para {nombre_persona}")

while True:
    ret, frame = cap.read()
    if not ret:
        break
    #Conversión a escala de grises
    #Reduce procesamiento y mejora detección facial
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    #Detecta las caras dentro del frame
    rostros = face_cascade.detectMultiScale(
        gray,
        scaleFactor=1.2,
        minNeighbors=5,
        minSize=(80, 80)
    )

    #Recorte del rostro
    for (x, y, w, h) in rostros:
        cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)

        if time.time() - ultimo_guardado >= intervalo:
            #Extrae únicamente la región facial
            rostro = gray[y:y+h, x:x+w]
            #Mejora de iluminación y contraste
            rostro = cv2.equalizeHist(rostro)
            #Todas las imágenes quedan del mismo tamaño para el entrenamiento
            rostro = cv2.resize(rostro, (160, 160))

            ruta = os.path.join(ruta_completa, f"rostro_{contador}.jpg")
            #Guarda automáticamente cada rostro capturado
            cv2.imwrite(ruta, rostro)

            contador += 1
            ultimo_guardado = time.time()

            print(f"Foto {contador}/{fotos_maximas}")

    cv2.imshow("Captura Dataset", frame)

    tecla = cv2.waitKey(1)
    if tecla == 27 or contador >= fotos_maximas:
        break

cap.release()
cv2.destroyAllWindows()

print("Captura finalizada.")