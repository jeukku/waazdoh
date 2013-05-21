;--------------------------------

!define APPNAME "Waazdoh Music"

; The name of the installer
Name "Waazdoh Music"

; The file to write
OutFile "waazdohsetup64.exe"

; The default installation directory
InstallDir $APPDATA\Waazdoh

; Request application privileges for Windows Vista
RequestExecutionLevel user

;--------------------------------

; Pages

;Page directory
;Page instfiles

;--------------------------------

; The stuff to install
Section "" ;No components page, name is not important	
	SetOutPath $INSTDIR\assets\img
	File "files\assets\img\logo.png" 
	File "files\assets\img\splash.png" 
	File "files\assets\img\icon.ico" 
	 
	SetOutPath $INSTDIR\assets\ini
	File "files\assets\ini\settings.ini" 
 
 	; Set output path to the installation directory.
	SetOutPath $INSTDIR
	 
	; Put file there
	File "files\Waazdoh.exe" 
	File "files\Ionic.Zip.dll"
 
	# Start Menu
	createDirectory "$SMPROGRAMS\${APPNAME}"
	createShortCut "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk" "$INSTDIR\waazdoh.exe" "" "$INSTDIR\assets\img\icon.ico" 
SectionEnd ; end the section

