# Notes 

## GIT:

### Command List:

* git add "filename.file" ~ *add a file to staging area*
* git add -p "filename.file" ~ *to add only part of the file*
* git status ~ *to see what is in the staging area*
* git branch "branch_name" ~ *to create a new branch*
* git checkout "branch_name" ~ *to switch to a branch*
* git push --set-upstream origin "branch_name" ~ *to push a new branch to the remote*
* git merge branch_name ~ *to merge a branch into the current branch (main)*

You can always UNDO a merge conflit and return to the previous state by using:

* git merge --abort ~ *to abort a merge, in pratica fa tornare allo stato precedente al merge, in modo che 
  si possa effettuare alcune modifiche al codice per evitare conflitti.* 
* git rebase --abort ~ *to abort a rebase*

### GitFlow: 

**git flow *cmd* help**

* git flow init ~ *to initialize git flow*
* git flow feature start "feature_name" ~ *to create a new feature branch*
* git flow feature finish "feature_name" ~ *to merge a feature branch into the develop branch*
* git flow release start "release_name" ~ *to create a new release branch*
* git flow release finish "release_name" ~ *to merge a release branch into the main branch*
* git flow hotfix start "hotfix_name" ~ *to create a new hotfix branch*
* git flow hotfix finish "hotfix_name" ~ *to merge a hotfix branch into the main branch*

Differenze tra merge e rebase:
* git merge ~ *mergea il branch corrente con il branch specificato, creando un nuovo commit*
* git rebase ~ *rebasea il branch corrente con il branch specificato, applicando i commit del branch specificato
  uno per uno sul branch corrente, senza creare un nuovo commit*

**WARNING**

Do not use Rebase on commits that you've already pushed/shared on a remote repository.

## Annotations:
@Transactional ~ *to make a method transactional*

## React and Tailwind:
.jsx ~ *creo un file dentro la cartella "components"*
rfc ~ per creare un componente react

## Maven
mvn clean package -DskipTests ~ run the test
./mvnw -DskipTests=true clean package ~ alternativa (?)

## Docker
mvn package ~ compilazione codice sorgente
docker build -t user-service:latest . ~ build the container
(oppure) docker build -t user-service:v1.1 .

## Database (with Docker)

Per controllare se le porte sono libere (linux):

``` bash
netstat -tuln | grep 5433
```

La sintassi delle porte è ```PORTA_HOST:PORTA_CONTAINER```, ad esempio ```5444:5432```.

Creiamo il db con docker

```bash
docker run --name user-db -e POSTGRES_DB=user-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -p 5432:5432 --network=host -d postgres
```

## Setup Docker

### Database

```bash
docker pull postgres
```

Dopo di che bisogna creare la network con docker:

```bash
docker network crate my-network
```

Dopo aver scaricato l'immagine di postgres da docker hub, bisogna lanciarlo con il seguente comando:

```bash
docker run --name users-db -e POSTGRES_DB=users1db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -p 5444:5432 --network=my-network -d postgres
```

Ora possiamo creare l'immagine del nostro microservizio:

```bash
docker build -t nome-microservizio:v1.2 .
```

E far girare il container:

```bash
docker run --name nome-microservizio --network=my-network -p 8080:8080 nome-microservizio:v1.2
```

**Attenzione!!**

Per far comunicare il DB con minikube serve esporlo esternamente a docker, di conseguenza si avvia con il seguente comando
_in modalita host_:

```bash
docker run --name users-db -e POSTGRES_DB=users1db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -p 5432:5432 --network host -d postgres
```

## Kubernetes

### Minikube

1. Install Minikube

2. Install Kubernetes
   - Follow [this tutorial guide](https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/)

3. Start Minikube

```bash
minikube start
```

4. Lanciare il file YML con le configurazioni di K8s

```bash
kubectl apply -f tuo-file-deployment.yml
```

### Operazioni di verifica

1. Stato Deployment 

```bash
kubectl get deployments
```

2. Verifica Pod

```bash
kubectl get pods
```

oppure

```bash
kubectl describe pod <nome-del-pod>
```

## Nota per Database con Kubertnetes

Per non integrare il db in K8s ma per comunicarci bisogna cambiare il tipo di connessione all'interno del microservizio, 
indicando l'indirzzo IP del database, che non sarà più
```/users-db:5432/users1db``` ma sarà qualcosa del genere: ```/<IP>:8080/user```
L'IP del DB che gira su docker può essere recuperato con il seguente comando

```bash
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' nome-del-container-db
```

## Docker push - Caricare image del microservizio su docker hub

```bash
docker login
```

Prima si fa la build dell'immagine:

```bash
docker build -t user-service:v1.4 .
```

Poi si tagga la version con:

```bash
docker tag user-service:v1.4 paolobonicco/user-service:v1.4
```

Dopo di che si pusha su dockerhub:

```bash
docker push paolobonicco/user-service:v1.4
```

Per trovare l'url del cluster k8s:

```bash
minikube service user-service --url 
```

Per aggiungere username e pwd del db su k8s:

```bash
kubectl create secret generic user-service-secrets \
  --from-literal=db-username=postgres \
  --from-literal=db-password=xxx
```

Per cancellarle:

```bash
kubectl delete secret user-service-secrets
```

