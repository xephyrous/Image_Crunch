$extension = if ($args.Count -eq 0) { "*.kt" } else { "*.$($args[0])" }

$totalLineCount = 0
$totalFileCount = 0

git ls-files $extension | % {
    $lineCount = (Get-Content $_ | Measure-Object -Line).Lines
    $totalLineCount += $lineCount
    $totalFileCount += 1
}

$extension = $extension.TrimStart('*.')

Write-Output "==========================================="
Write-Output "  [ Line Count for $totalFileCount .$extension Files ] : $totalLineCount"
Write-Output "==========================================="
