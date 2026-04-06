import cv2
import os

# --- CONFIGURACIÓN ---
dataset_dir = '../data/02_processed'  
nombre_persona = 'Gabriel' 
ruta_completa = os.path.join(dataset_dir, nombre_persona)
fotos_maximas = 400

# Crear la carpeta si no existe
if not os.path.exists(ruta_completa):
    os.makedirs(ruta_completa)
    print(f"Carpeta creada: {ruta_completa}")

# Cargar el modelo preentrenado de OpenCV 
cascade_path = cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'
face_cascade = cv2.CascadeClassifier(cascade_path)

# Iniciar la cámara
cap = cv2.VideoCapture(0)

contador = 0
print(f"Iniciando captura para {nombre_persona}... Mira a la cámara.")

while True:
    # Leer el frame de la cámara
    ret, frame = cap.read()
    if not ret:
        print("Error al acceder a la cámara.")
        break

    # Convertir el frame a escala de grises (mejora la precisión de la detección del rostro)
    grises = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Detectar rostros en el frame
    rostros = face_cascade.detectMultiScale(grises, scaleFactor=1.3, minNeighbors=5)

    for (x, y, w, h) in rostros:
        # Dibujar rectángulo para que veas qué está capturando
        cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 124, 124), 2)

        # Recortar solo la región del rostro
        rostro_recortado = frame[y:y+h, x:x+w]
        
        # Redimensionar a 160x160 píxeles
        rostro_redimensionado = cv2.resize(rostro_recortado, (160, 160))

        # Guardar la imagen en la carpeta
        nombre_archivo = f"{ruta_completa}/rostro_{contador}.jpg"
        cv2.imwrite(nombre_archivo, rostro_redimensionado)
        
        contador += 1
        print(f"Capturando foto {contador}/{fotos_maximas}")

    # Mostrar la ventana en vivo
    cv2.imshow('Captura de Dataset Facial', frame)

    # El programa se detiene de dos formas:
    # 1. Si llega al límite de fotos máximas
    # 2. Si presionas la tecla 'ESC' (código 27)
    tecla = cv2.waitKey(1)
    if tecla == 27 or contador >= fotos_maximas:
        break

# Limpieza y cierre
print("Captura finalizada con éxito.")
cap.release()
cv2.destroyAllWindows()
