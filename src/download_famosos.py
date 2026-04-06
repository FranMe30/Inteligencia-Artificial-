from icrawler.builtin import BingImageCrawler
import os

# 1. Definir los famosos
famosos = [
    "Scarlett Johansson"
    #"Lionel Messi",
    #"Canelo Alvarez",
    #"Taylor Swift",
    #"Jared Borgetti",
    #"Cristiano Ronaldo"
]

# 2. Definir "modificadores" de búsqueda para engañar al límite
# Esto multiplicará tus resultados.
modificadores = ["2018", "2019", "2020"]

# 3. Ruta de destino base
base_dir = "../data/01_raw/famososSinProcesar"
os.makedirs(base_dir, exist_ok=True)

# 4. Bucle de descarga
for personaje in famosos:
    print(f"\n=======================================")
    print(f"Iniciando recolección masiva: {personaje}")
    print(f"=======================================")
    
    folder_name = personaje.replace(" ", "_")
    output_dir = os.path.join(base_dir, folder_name)
    os.makedirs(output_dir, exist_ok=True)
    
    # Bucle interno para hacer varias búsquedas del mismo personaje
    for mod in modificadores:
        # Contar cuántas imágenes hay actualmente en la carpeta para no sobrescribir
        imagenes_actuales = len([name for name in os.listdir(output_dir) if os.path.isfile(os.path.join(output_dir, name))])
        
        # Si ya llegamos a la meta de 300 (o más), saltamos al siguiente famoso
        if imagenes_actuales >= 300:
            print(f"¡Meta de 300 imágenes alcanzada para {personaje}!")
            break
            
        termino_busqueda = f"{personaje} {mod}"
        print(f"-> Buscando variación: '{termino_busqueda}' (Imágenes actuales: {imagenes_actuales})")
        
        try:
            bing_crawler = BingImageCrawler(
                downloader_threads=4,
                storage={'root_dir': output_dir}
            )
            
            # Pedimos 80 por cada variación y usamos el número de imágenes actuales como offset
            bing_crawler.crawl(
                keyword=termino_busqueda, 
                max_num=80, 
                file_idx_offset=imagenes_actuales
            )
        except Exception as e:
            print(f"Error en la búsqueda '{termino_busqueda}': {e}. Continuando con la siguiente variación...")
            continue

print("\n¡Descarga masiva completada al 100%!")