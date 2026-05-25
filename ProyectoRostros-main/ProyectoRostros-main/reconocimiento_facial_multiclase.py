import cv2
import os
import numpy as np

# ==========================================
# CONFIGURACIÓN
# ==========================================
DATASET_DIR = 'data/02_processed'
MODEL_FILE = 'modelo_lbph.yml'
LABELS_FILE = 'etiquetas.npy'
IMAGE_SIZE = (160, 160)

# Umbral de confianza:
# Valores más bajos = más estricto
# Valores más altos = más permisivo
#Define si el rostro será reconocido o marcado como desconocido
CONFIDENCE_THRESHOLD = 75

# Lista de personas permitidas para el reconocimiento
PERSONAS_PERMITIDAS = [
    # Alumnos
    "Alumno_Bernardo",
    "Alumno_Gabriel",
    "Alumno_Lanna",
    "Alumno_Francisco",

    # Famosos 
    "Brad Pitt",
    "Hugh Jackman",
    "Jennifer Lawrence",
    "Leonardo DiCaprio",
    "Megan Fox",
    "Robert Downey Jr"
]

# ==========================================
# CARGAR DATASET
# ==========================================
# Carga las imágenes, asigna etiquetas numéricas y crea un mapa de etiquetas.
def cargar_dataset():
    faces = []
    labels = []
    label_map = {}
    current_label = 0

    print("Cargando dataset...\n")

    if not os.path.exists(DATASET_DIR):
        print(f"ERROR: No existe la carpeta {DATASET_DIR}")
        return [], np.array([]), {}

    for person_name in os.listdir(DATASET_DIR):

        # Solo procesar las personas permitidas
        if person_name not in PERSONAS_PERMITIDAS:
            continue

        person_path = os.path.join(DATASET_DIR, person_name)

        if not os.path.isdir(person_path):
            continue

        print(f"Procesando: {person_name}")

        label_map[current_label] = person_name
        count = 0

        for file in os.listdir(person_path):

            file_lower = file.lower()

            # Aceptar formatos comunes de imagen
            if not file_lower.endswith(('.jpg', '.jpeg', '.png', '.jfif', '.bmp')):
                continue

            img_path = os.path.join(person_path, file)

            try:
                # Leer imagen en escala de grises
                img = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)

                if img is None:
                    print("No se pudo leer:", img_path)
                    continue

                # Normalizar imagen
                img = cv2.equalizeHist(img)
                img = cv2.resize(img, IMAGE_SIZE)

                faces.append(img)
                labels.append(current_label)
                #Guarda imágenes y etiquetas

                count += 1

            except Exception as e:
                print("Error con imagen:", img_path, e)
                continue

        print(f"  -> imágenes cargadas: {count}")

        current_label += 1

    print("\n================================")
    print(f"Total imágenes: {len(faces)}")
    print(f"Clases: {len(label_map)}")
    print("================================\n")

    return faces, np.array(labels), label_map


# ==========================================
# ENTRENAR MODELO
# ==========================================
def entrenar_modelo():
    faces, labels, label_map = cargar_dataset()

    if len(faces) == 0:
        print("ERROR: No hay imágenes para entrenar.")
        return None, None
    
    #Crea el modelo LBPH y lo entrena con las imágenes y etiquetas cargadas
    #Local Binary Patterns Histograms
    #Analiza texturas y patrones faciales
    recognizer = cv2.face.LBPHFaceRecognizer_create()
    #El modelo aprende patrones faciales
    recognizer.train(faces, labels)
    #Guarda el entrenamiento realizado
    recognizer.save(MODEL_FILE)
    #Relaciona nombres con IDs internos
    np.save(LABELS_FILE, label_map)

    print("Modelo entrenado correctamente.")
    print(f"Modelo guardado en: {MODEL_FILE}")
    print(f"Etiquetas guardadas en: {LABELS_FILE}")

    return recognizer, label_map


# ==========================================
# CARGAR MODELO
# ==========================================
def cargar_modelo():
    if not os.path.exists(MODEL_FILE) or not os.path.exists(LABELS_FILE):
        print("No se encontró el modelo entrenado. Entrenando automáticamente...")
        return entrenar_modelo()

    recognizer = cv2.face.LBPHFaceRecognizer_create()
    #Carga el modelo previamente entrenado
    recognizer.read(MODEL_FILE)

    #Carga nombres asociados a IDs
    label_map = np.load(LABELS_FILE, allow_pickle=True).item()

    print("Modelo cargado correctamente.")
    return recognizer, label_map


# ==========================================
# RECONOCIMIENTO EN TIEMPO REAL
# ==========================================
def reconocer():
    recognizer, label_map = cargar_modelo()

    if recognizer is None:
        return

    face_cascade = cv2.CascadeClassifier(
        cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'
    )
    #Activa reconocimiento en tiempo real usando la cámara web
    cap = cv2.VideoCapture(0)

    if not cap.isOpened():
        print("ERROR: No se pudo abrir la cámara.")
        return

    print("Cámara iniciada. Presiona 'q' para salir.")

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # Convertir a escala de grises
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # Detectar rostros
        #Detecta rostros dentro del frame usando el clasificador Haar Cascade
        faces = face_cascade.detectMultiScale(
            gray,
            scaleFactor=1.2,
            minNeighbors=5,
            minSize=(80, 80)
        )

        for (x, y, w, h) in faces:
            try:
                # Extraer rostro
                face = gray[y:y+h, x:x+w]

                # Normalizar igual que en entrenamiento
                face = cv2.equalizeHist(face)
                face = cv2.resize(face, IMAGE_SIZE)

                # Predicción, Predice identidad y confianza usando el modelo entrenado
                label, confidence = recognizer.predict(face)

                # Si la confianza es buena y la etiqueta existe,
                # mostrar nombre; en otro caso, "Desconocido"
                if confidence < CONFIDENCE_THRESHOLD and label in label_map:
                    name = label_map[label]
                else:
                    name = "Desconocido"

                # Texto con confianza
                texto = f"{name} ({confidence:.1f})"

                # Color:
                # Verde = reconocido
                # Rojo = desconocido
                color = (0, 255, 0) if name != "Desconocido" else (0, 0, 255)

                # Dibujar rectángulo y texto
                #Muestra nombre y recuadro del rostro en pantalla
                cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)

                cv2.putText(
                    frame,
                    texto,
                    (x, y - 10),
                    cv2.FONT_HERSHEY_SIMPLEX,
                    0.7,
                    color,
                    2
                )

            except Exception as e:
                print("Error durante reconocimiento:", e)
                continue

        # Mostrar ventana
        cv2.imshow("Reconocimiento Facial", frame)

        # Salir con 'q'
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Liberar recursos
    cap.release()
    cv2.destroyAllWindows()


# ==========================================
# MAIN
# ==========================================
if __name__ == "__main__":
    reconocer()