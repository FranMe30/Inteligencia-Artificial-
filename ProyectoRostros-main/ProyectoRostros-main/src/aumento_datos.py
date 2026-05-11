import cv2
import os
import glob
import random
import numpy as np

# --- CONFIGURACIÓN ---
carpeta_raiz = '../data/02_processed'  

def aplicar_transformacion_aleatoria(img):
    """Aplica transformaciones seguras para reconocimiento facial"""
    
    # 1. Espejado horizontal (50% de probabilidad)
    if random.random() > 0.5:
        img = cv2.flip(img, 1) # 1 significa eje Y (horizontal)
        
    # 2. Ajuste de brillo (aleatorio entre oscurecer un poco y aclarar un poco)
    valor_brillo = random.randint(-40, 40)
    img_brillo = np.int16(img) + valor_brillo
    img_brillo = np.clip(img_brillo, 0, 255) 
    img = np.uint8(img_brillo)
    
    # 3. Rotación leve (aleatoria entre -10 y 10 grados)
    angulo = random.uniform(-10, 10)
    h, w = img.shape[:2]
    centro = (w // 2, h // 2)
    matriz_rotacion = cv2.getRotationMatrix2D(centro, angulo, 1.0)
    img = cv2.warpAffine(img, matriz_rotacion, (w, h), borderMode=cv2.BORDER_REPLICATE)
    
    return img

# Obtener solo las carpetas dentro del directorio raíz
carpetas_personas = [nombre for nombre in os.listdir(carpeta_raiz) 
                   if os.path.isdir(os.path.join(carpeta_raiz, nombre))]

print("Iniciando Aumento de Datos (Duplicando dataset)...")
print("-" * 40)

for persona in carpetas_personas:
    ruta_persona = os.path.join(carpeta_raiz, persona)
    
    # Buscar las imágenes originales
    archivos_imagenes = glob.glob(os.path.join(ruta_persona, '*.jpg'))
    
    # Filtrar para no procesar imágenes que ya hayan sido aumentadas antes
    archivos_originales = [f for f in archivos_imagenes if '_aug_' not in f]
    
    print(f"Procesando {persona}: {len(archivos_originales)} imágenes originales encontradas.")
    
    contador_nuevas = 0
    
    for ruta_imagen in archivos_originales:
        img = cv2.imread(ruta_imagen)
        
        if img is None:
            continue
            
        # Generar la nueva imagen alteradaz
        img_aumentada = aplicar_transformacion_aleatoria(img)
        
        # Guardar la nueva imagen con un sufijo "_aug_"
        nombre_base = os.path.basename(ruta_imagen).replace('.jpg', '')
        nueva_ruta = os.path.join(ruta_persona, f"{nombre_base}_aug_{contador_nuevas}.jpg")
        
        cv2.imwrite(nueva_ruta, img_aumentada)
        contador_nuevas += 1
        
    print(f"  -> Se generaron {contador_nuevas} imágenes nuevas. Total ahora: {len(archivos_originales) + contador_nuevas}")

print("-" * 40)
print("¡Proceso completado! Tu dataset ha sido duplicado de forma segura.")