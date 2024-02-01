<#
 # This script adds a KDoc comment to theme lists in a file.
 # This is done automatically for the whole file, it will generate a theme name based on the name of the list.
 # Before running this script add a connet directly above the list in the following format:
 #
 # /* THEME MARKER
 # [NAME OF COLOR ELEMENT 1]
 # [NAME OF COLOR ELEMENT 2]
 # ...
 # */
 #
 # Then run the following command in the terminal (from root):
 # ./docs/themeKDoc.ps1 [path to file]
 #
 # Ex. "./docs/themeKDoc.ps1 /src/main/kotlin/utils/theme.kt "
 #>


$file_path = (Get-Location).ToString() + $args[0]
$original_file = Get-Content $file_path
$modified_file = @()
$parserLineNo = 0

function Exit-Error($errorMessage) {
    throw ("[ERROR] | Line " + $parserLineNo + " | " + $errorMessage)
}

function Add-Comment($lineNo) {
    $lineNo++

    #Line position trackers
    $parserLineNo = 0
    $linePos = 0

    #Element arrays
    $colorArray = @()
    $nameArray = @()
    $theme = ""

    #State booleans
    $findLine = $true
    $readNames = $false
    $readColors = $false

    #Read names and colors into arrays
    foreach($line in $original_file) {
        #Trim leading and trailing whitespace
        $line = $line.Trim()

        #Find line position
        if($findLine -and !($linePos -eq $lineNo)) {
            $linePos++
            $parserLineNo++
            continue
        } else {
            if(!$readNames -and !$readColors) {
                $readNames = $true
                $findLine = $false
            }
        }

        #Read names
        if($readNames -and !($line -contains "*/")) {
            if($line -match "val") { Exit-Error("Theme Marker End Brace Missing!") }
            $nameArray += $line;
            $parserLineNo++
            continue
        } else {
            $readNames = $false;
            $readColors = $true;
        }

        #Read Colors
        if($readColors) {

            #Read theme name
            if(($line -match "Themes") -and ($theme -eq "")) {
                $parserLineNo++
                $theme = $line.SubString(4, $Line.IndexOf("Themes") - 4)
                $theme = (Get-Culture).TextInfo.ToTitleCase($theme)
                continue
            }
            if($line -contains [Regex]::Escape(")")) { break }
            try {
                $parserLineNo++
                $colorArray += $line.Substring($line.IndexOf("(") + 1, 10)
            } catch { }
        }
    }



    #Build comment
    $linePos = 0
    $commentArray = @()

    $commentArray += "/**"
    $commentArray += (" * " + $theme + " Application Theme")

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
    if($skip -and ($line -contains "*/")) { $skip = $false; $lineNo++; continue }
    if($skip) { $lineNo++; continue }

    #Match a theme marker tag and append generated comment
    if($line -contains "/* THEME MARKER") {
        $comment = Add-Comment $lineNo

        foreach($commentLine in $comment) {
            $modified_file += $commentLine
        }

        $skip = $true
    } else {
        $modified_file += $line
    }

    $lineNo++
}

#Rewrite file with comments added
Clear-Content $file_path
$modified_file | Out-File $file_path