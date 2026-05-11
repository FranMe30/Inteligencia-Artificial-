import tensorflow as tf
from tensorflow.keras import layers, models
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import numpy as np

IMG_SIZE = (160, 160)
BATCH_SIZE = 32

train_datagen = ImageDataGenerator(rescale=1./255, rotation_range=20, horizontal_flip=True)
val_datagen = ImageDataGenerator(rescale=1./255)

train_gen = train_datagen.flow_from_directory('train', target_size=IMG_SIZE, batch_size=BATCH_SIZE, class_mode='sparse')
val_gen = val_datagen.flow_from_directory('val', target_size=IMG_SIZE, batch_size=BATCH_SIZE, class_mode='sparse')

base_model = tf.keras.applications.MobileNetV2(input_shape=(160,160,3), include_top=False, weights='imagenet')
base_model.trainable = False

model = models.Sequential([
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dense(len(train_gen.class_indices), activation='softmax')
])

model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])

model.fit(train_gen, validation_data=val_gen, epochs=5)

model.save('celebrity_model.h5')
class_names = list(train_gen.class_indices.keys())
np.save('class_names.npy', class_names)

print("Clases encontradas:", class_names)
print("Entrenamiento completado. Modelo guardado como 'celebrity_model.h5'")