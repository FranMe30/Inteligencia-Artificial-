import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
from tensorflow.keras.preprocessing import image

def predict_celebrity(img_path, model_path='celebrity_model.h5', classes_path='class_names.npy'):
    # Cargar modelo y nombres de clases
    model = tf.keras.models.load_model(model_path)
    class_names = np.load(classes_path, allow_pickle=True).tolist()
    
    # Cargar y preprocesar la imagen
    img = image.load_img(img_path, target_size=(160, 160))
    img_array = image.img_to_array(img) / 255.0
    img_array = np.expand_dims(img_array, axis=0)
    
    # Predecir
    predictions = model.predict(img_array)
    score = np.max(predictions)
    class_idx = np.argmax(predictions)
    
    # Mostrar resultado
    plt.imshow(img)
    plt.title(f"Predicción: {class_names[class_idx]} ({100 * score:.2f}%)")
    plt.axis('off')
    plt.show()
    
    print(f"Predicción: {class_names[class_idx]} con confianza {100*score:.2f}%")

# Reemplaza 'prueba.jpg' por la ruta de una imagen real
if __name__ == "__main__":
    # Por ejemplo, usa la primera imagen de la carpeta val/ben_afflek
    import os
    test_image = 'val/ben_afflek/' + os.listdir('val/ben_afflek')[0]  # toma la primera imagen
    predict_celebrity(test_image)