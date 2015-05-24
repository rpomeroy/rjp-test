node('master') {
    env.PATH="${tool 'M3'}/bin:${env.PATH}"
    echo "path is: ${env.PATH}"
}
