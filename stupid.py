import keyboard
import pyautogui
import time

paste = False
keyboard.add_hotkey('ctrl+q', quit)

while True:
    if keyboard.is_pressed('ctrl+p'):
        paste = not paste

    if paste:
        pyautogui.typewrite('Τρως στεριαΠερι\n')