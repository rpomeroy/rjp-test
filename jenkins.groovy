node('master') {
    // See if we can cause maven to download
    env.PATH="${tool 'maven_3.2.2'}/bin:${env.PATH}"
    echo "path is: ${env.PATH}"
}
