def call() {
  Map pipelineConfig = readYaml(file: "${env.WORKSPACE}/Jenkinsfile.yaml")
  node {
    stage('Build'){
      println "Building: ${pipelineConfig.project_name}"
    }
  }
}  
