param(
    [string]$SourceFile
)

if(-not (Test-Path $SourceFile)){
	Write-Error "Source file not found: $SourceFile"
	exit 1
}

if (-not $SourceFile.EndsWith(".cow")) {
    Write-Error "Expected a .cow source file."
    exit 1
}

$baseName = [System.IO.Path]::GetFileNameWithoutExtension($SourceFile)
$sourceDirectory = [System.IO.Path]::GetDirectoryName($SourceFile)

if ([string]::IsNullOrEmpty($sourceDirectory)) {
    $sourceDirectory = "."
}



$llvmFile = Join-Path $sourceDirectory "$baseName.ll"
$bitcodeFile = Join-Path $sourceDirectory "$baseName.bc"
$exeFile = Join-Path $sourceDirectory "$baseName.exe"

$appDir = Join-Path $PSScriptRoot "app"
$absoluteSource = (Resolve-Path $SourceFile).Path

Push-Location $appDir
$compilerSource = Resolve-Path $absoluteSource -Relative

Pop-Location

Write-Host "Running cowlang compiler..."
.\gradlew.bat run --args="$compilerSource"


if ($LASTEXITCODE -ne 0) {
    Write-Error "cowlang compiler failed."
    exit $LASTEXITCODE
}

Write-Host "Assembling LLVM IR..."
llvm-as $llvmFile -o $bitcodeFile

if ($LASTEXITCODE -ne 0) {
    Write-Error "LLVM assembler failed."
    exit $LASTEXITCODE
}

Write-Host "Creating executable..."

clang $bitcodeFile -o $exeFile

if ($LASTEXITCODE -ne 0) {
    Write-Error "Native compilation failed."
    exit $LASTEXITCODE
}
