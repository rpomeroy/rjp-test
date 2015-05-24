node('master') {
    // See if we can cause maven to download
    env.PATH="${tool 'M3'}/bin:${env.PATH}"
    echo "path is: ${env.PATH}"
}
