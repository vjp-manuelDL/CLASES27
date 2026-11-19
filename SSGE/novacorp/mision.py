# NovaCorp · Sesión 1 · Tu script de investigación
# Guárdalo en la misma carpeta que datos.py y ejecútalo con:  python mision.py
from datos import empleados, accesos

print("Empleados en la plantilla:", len(empleados))
print("Primer empleado:", empleados[0])
print("Su nombre:", empleados[0]["nombre"])
print("Primer acceso:", accesos[0])

# A partir de aquí, tu código. Una parte por misión:

# Misión 2 · La nómina fantasma

# Misión 3 · El intruso

# ...


print("--- Todos los empleado ---")
for empleado in empleados:
    print(empleado["nombre"], empleado["depto"], empleado["salario"]) 



print("--- Todos los accesos ---")
for acceso in accesos:
    print("Empleado:", acceso[0], "| Hora:", acceso[1], "| IP:", acceso[2])



print ("--- SOLO LOS DE FINANZAS ---")
for empleado in empleados:
    if empleado ["depto"] == "Finanzas":
        print (empleado["id"],empleado["nombre"],empleado["salario"])