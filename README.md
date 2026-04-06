# ProyectoRostros - Reconocimiento Facial

Este proyecto permite capturar rostros, descargar imágenes de famosos, procesarlas y aumentar el dataset mediante transformaciones de datos.

## 📋 Requisitos Previos

1. Tener Python 3.7+ instalado
2. Poseer una cámara conectada (para la captura de rostros)
3. Conexión a Internet (para descargar imágenes de famosos)

## 🔧 Instalación
```bash
pip install -r requirements.txt
```

---
## 📸 Paso 1: Capturar Rostros del Usuario

**Archivo:** `src/captura_rostros.py`

Este script captura imágenes de tu rostro usando la cámara web y las guarda en tamaño 160x160 píxeles.

### ¿Qué hace?
- Abre tu cámara
- Detecta tu rostro automáticamente
- Guarda las imágenes recortadas (solo el rostro)
- Las almacena en: `data/02_processed/nombre/`

### ¿Cómo usarlo?
```bash
cd src
python captura_rostros.py
```

### Instrucciones:
1. Mira directamente a la cámara
2. Intenta capturar al menos **400 imágenes** (se capturan automáticamente)
3. Presiona **'q'** para salir cuando termines
4. Las imágenes se guardarán automáticamente

**📝 Nota:** Cambia la variable `nombre_persona = 'Nombre'` en el script si quieres cambiar el nombre de la carpeta de salida.

---

## 🌍 Paso 2: Descargar Imágenes de Famosos en Bruto

**Archivo:** `src/download_famosos.py`

Este script descarga imágenes de internet de celebridades famosas usando Bing Image Search.

### ¿Qué hace?
- Descarga ~300 imágenes de cada famoso
- Las almacena en: `data/01_raw/famososSinProcesar/`
- Crea carpetas automáticamente por cada persona

### ¿Cómo usarlo?

```bash
cd src
python download_famosos.py
```

### Famosos incluidos por defecto:
- Scarlett Johansson
- Lionel Messi
- Canelo Alvarez
- Taylor Swift
- Jared Borgetti
- Cristiano Ronaldo

### Personalización:
Para agregar o cambiar famosos, edita la lista `famosos` en el script:

```python
famosos = [
    "Tu Celebridad Aqui",
    "Otra Celebridad",
]
```

**⏱️ Nota:** Este proceso toma tiempo (depende de tu conexión a Internet). Puede durar 5-20 minutos.

---

## 🎯 Paso 3: Procesar Imágenes de Famosos

**Archivo:** `src/procesar_famosos.py`

Este script extrae los rostros de las imágenes descargadas y las recorta a 160x160 píxeles.

### ¿Qué hace?
- Lee las imágenes en bruto de `data/01_raw/famososSinProcesar/`
- Detecta rostros usando cascada de Haar
- Recorta solo los rostros encontrados
- Los guarda en: `data/02_processed/`
- Ignora imágenes sin rostros o que no se pueden procesar

### ¿Cómo usarlo?

```bash
cd src
python procesar_famosos.py
```

### Resultado esperado:
- Verás progreso por cada famoso procesado
- Se mostrará cuántas imágenes se guardaron
- Las imágenes procesadas estarán listas para el siguiente paso

---

## 📈 Paso 4: Aumento de Datos (Data Augmentation)

**Archivo:** `src/aumento_datos.py`

Este script duplica tu dataset aplicando transformaciones aleatorias a las imágenes existentes.

### ¿Qué hace?
- Lee imágenes procesadas de `data/02_processed/`
- Aplica transformaciones aleatorias:
  - ✓ Espejado horizontal (50% de probabilidad)
  - ✓ Ajuste de brillo aleatorio
  - ✓ Rotación leve (-10 a +10 grados)
- Guarda las nuevas imágenes con sufijo `_aug_`
- **Multiplica tu dataset por 10 para cada persona**

### ¿Cómo usarlo?

```bash
cd src
python aumento_datos.py
```

### Ventajas:
- Aumenta significativamente el dataset sin necesidad de capturar más fotos
- Mejora la robustez del modelo de reconocimiento facial
- Transforma leves los datos manteniendo la identidad de la persona

---

## 📁 Estructura de Carpetas

```
ProyectoRostros/
├── data/
│   ├── 01_raw/
│   │   └── famososSinProcesar/      (Imágenes descargadas sin procesar)
│   │       ├── Scarlett_Johansson/
│   │       ├── Lionel_Messi/
│   │       └── ...
│   ├── 02_processed/                (Rostros recortados a 160x160)
│   │   ├── Lanna/
│   │   ├── Scarlett_Johansson/
│   │   ├── Lionel_Messi/
│   │   └── ...
│   └── 03_final/                    (Datos finales para entrenar modelo)
├── src/
│   ├── captura_rostros.py
│   ├── download_famosos.py
│   ├── procesar_famosos.py
│   └── aumento_datos.py
└── requirements.txt
```

---

## 🚀 Flujo Completo Recomendado

```
1. Instala dependencias
   ↓
2. Captura tus rostros (captura_rostros.py)
   ↓
3. Descarga imágenes de famosos (download_famosos.py) ← ESPERA AQUÍ
   ↓
4. Procesa las imágenes (procesar_famosos.py)
   ↓
5. Aumenta datos (aumento_datos.py)
   ↓
✅ Dataset listo para entrenar modelo de reconocimiento facial
```

---

## ⚠️ Troubleshooting

### "Error al acceder a la cámara"
- Verifica que tu cámara web está conectada
- Cierra otras aplicaciones que usen la cámara
- Reinicia el script

### "No se encuentran rostros"
- Asegúrate de que tu rostro es claramente visible
- Mejora la iluminación
- Acércate más a la cámara

### "Las imágenes no se descargan"
- Verifica tu conexión a Internet
- Intenta ejecutar más tarde (los servidores pueden estar llenos)
- Algunos famosos pueden no tener muchas imágenes disponibles

### "El procesamiento es muy lento"
- Esto es normal si hay muchas imágenes
- El tiempo depende de tu procesador
- Puedes pausar y continuar después

---

## 📝 Notas Importantes

- **Privacidad:** Las imágenes de tu rostro se guardan **localmente** en tu computadora
- **Almacenamiento:** Aumentar datos multiplica el tamaño de carpetas (prepara ~5-10 GB)
- **Requisitos de GPU:** Para entrenar el modelo después, se recomienda una GPU (NVIDIA CUDA)

---

## 📧 Soporte

Si encuentras problemas, verifica:
1. Que Python 3.7+ esté instalado
2. Que todas las librerías se instalaron correctamente (`pip install -r requirements.txt`)
3. Que tienes permiso de lectura/escritura en las carpetas `data/`

