import cv2
import os

# --- CONFIGURACIÓN DE RUTAS ---
# Carpeta donde están las fotos originales que descargaste
input_dir = '../data/01_raw/famososSinProcesar'

# Nueva carpeta donde se guardarán SOLAMENTE los rostros recortados a 160x160
output_dir = '../data/02_processed/'

# Cargar el modelo preentrenado de OpenCV 
cascade_path = cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'
face_cascade = cv2.CascadeClassifier(cascade_path)

# Asegurarnos de que el directorio de entrada exista
if not os.path.exists(input_dir):
    print(f"Error: No se encontró la carpeta {input_dir}")
    exit()

print("Iniciando extracción masiva de rostros...")

# 1. Recorrer cada subcarpeta (cada famoso) en el directorio de entrada
for nombre_personaje in os.listdir(input_dir):
    ruta_personaje_crudo = os.path.join(input_dir, nombre_personaje)
    
    # Ignorar si no es una carpeta
    if not os.path.isdir(ruta_personaje_crudo):
        continue

    # Crear la carpeta de destino para este famoso en específico
    ruta_personaje_procesado = os.path.join(output_dir, nombre_personaje)
    os.makedirs(ruta_personaje_procesado, exist_ok=True)
    
    contador = 0
    print(f"\nProcesando imágenes de: {nombre_personaje}...")

    # 2. Recorrer cada imagen dentro de la carpeta del famoso
    for nombre_imagen in os.listdir(ruta_personaje_crudo):
        ruta_imagen = os.path.join(ruta_personaje_crudo, nombre_imagen)
        
        # Leer la imagen
        imagen = cv2.imread(ruta_imagen)
        
        # Si OpenCV no puede leer la imagen (archivo corrupto o formato no soportado), la saltamos
        if imagen is None:
            continue

        # Convertir a escala de grises para el detector
        grises = cv2.cvtColor(imagen, cv2.COLOR_BGR2GRAY)

        # Detectar rostros
        rostros = face_cascade.detectMultiScale(grises, scaleFactor=1.2, minNeighbors=5)

        # 3. Recortar y guardar cada rostro encontrado
        for (x, y, w, h) in rostros:
            # Recortar solo la región del rostro
            rostro_recortado = imagen[y:y+h, x:x+w]
            
            try:
                # Redimensionar a 160x160 píxeles
                rostro_redimensionado = cv2.resize(rostro_recortado, (160, 160))
                
                # Guardar la imagen en la nueva carpeta
                nombre_archivo = f"rostro_{contador}.jpg"
                ruta_guardado = os.path.join(ruta_personaje_procesado, nombre_archivo)
                cv2.imwrite(ruta_guardado, rostro_redimensionado)
                
                contador += 1
            except Exception as e:
                continue

    print(f"-> {contador} rostros extraídos con éxito para {nombre_personaje}.")

print("\n¡Extracción y procesamiento finalizado! Revisa la carpeta:", output_dir)