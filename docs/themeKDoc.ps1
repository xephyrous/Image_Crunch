$file_path = (Get-Location).ToString() + $args[0]
$original_file = Get-Content $file_path
$modified_file = @()

function Add-Comment($lineNo) {
    $lineNo++

    $linePos = 0
    $colorArray = @()
    $nameArray = @()

    $findLine = $true
    $readNames = $false
    $readColors = $false

    #Read names and colors into arrays
    foreach($line in $original_file) {

        #Find line position
        if($findLine -and !($linePos -eq $lineNo)) {
            $linePos++
            continue
        } else {
            if(!$readNames -and !$readColors) {
                $readNames = $true
                $findLine = $false
            }
        }

        #Read names
        if($readNames -and !($line -contains "*/")) {
            $nameArray += $line;
            continue
        } else {
            $readNames = $false;
            $readColors = $true;
            continue
        }

        #Read Colors
        if($readColors) {
            if($line -match "Themes") { continue }
            if(!($line -match "Color(0x")) { break }
            $colorArray += $line.Substring($line.IndexOf("x") - 1, 9)
        }
    }

    #Build comment
    $linePos = 0
    $commentArray = @()

    $commentArray += "/**"
    $commentArray += " * [THEME NAME] Application Theme"

    foreach($name in $nameArray) {
        $commentArray += " * <p style=""background-color:#ffffff;font-weight:bold""><font color=""" +
                        $colorArray[$linePos] + """> > " + $nameArray[$linePos] + " Color </p>"
        $linePos++
    }

    $commentArray += "*/"

    return $commentArray
}

$lineNo = 0
$skip = $false
foreach($line in $original_file) {

    #Skip comment names
    if($skip -and ($line -contains "*/")) {
        $skip = $false
        continue
    } else {
        if($skip) { continue }
    }

    #Match a theme marker tag and append generated comment
    if($line -contains "/* THEME MARKER") {
        $comment = Add-Comment $lineNo
        Write-Output $comment
        foreach($cLine in $comment) { $modified_file += $cLine }
        $skip = $true
    } else {
        $modified_file += $line
    }

    $lineNo++
}

#Write-Output $modified_file