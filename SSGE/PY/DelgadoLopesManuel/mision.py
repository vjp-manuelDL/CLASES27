# NovaCorp · Sesión 1 · Tu script de investigación
# Guárdalo en la misma carpeta que datos.py y ejecútalo con:  python mision.py
from datos import empleados, accesos

print("Empleados en la plantilla:", len(empleados))
print("Primer empleado:", empleados[0])
print("Su nombre:", empleados[0]["nombre"])
print("Primer acceso:", accesos[0])

# A partir de aquí, tu código. Una parte por misión:

# Misión 2 · La nómina fantasma
#bucle que suma el salario de cada empleado al una variable para obtener el total 
total = 0
for e in empleados:
    total += e["salario"]
print("Total de la nómina:", total)

# Misión 3 · El intruso

#bucle para encontrar el acceso sospechoso
for a in accesos:
    ip = a[2]
    if not ip.startswith("10."):
        print("Acceso sospechoso:", a)
        print("Nombre del empleado:", a[0])
        
        numero = a[0]
        for e in empleados:
            if e["id"] == numero:
                print("Nombre del empleado:", e["nombre"])

# Misión 4 · Rastro digital
#buvcle para encontrar la ip del intruso , compara la id con el numero antes sacado
for a in accesos:
    i = a[0]
    if i == numero:
        print("IP del intruso:", a[2])


# Misión 5 · Hora cero

# bucle que comprara la id con el numero para sacar la hora
for a in accesos:
    if a[0] == numero:
        fecha = a[1] 
print("Hora del acceso sospechoso:", fecha)# solo hace print de la ultima fecha que coincide con la id del intruso
print("Solo la hora:", fecha[11:16]) # solo la hora porque lo pone en enunciado, por si acaso ( aunque hay que entregar fecha y hora)



# Misión 6 · La caja fuerte
total = 0
#bucle en empleados para sumar el salario de los empleados del departamento de sistemas 
for e in empleados:
    if e["depto"] == "Sistemas":
        total += e["salario"]
print("Total de la nómina del departamento de Sistemas:", total)
print ("Slario medio del total de Sistemas:", total / 2)  #aqui saco la mitad del total (total/2)

# 7	Misión 7 · Fuera de horario
total = 0
#bucle que compara la hora de cada acceso con el tramo permitido 08:00 a 22:00 
for a in accesos:
    fecha = a[1]
    if fecha[11:16] <"08:00 " or fecha[11:16] > "22:00":
        total += 1  # aqui sumo 1 por cada acceso no permitido 
print("Número de accesos fuera de horario:", total)

