1. Create Deployment
kubectl create deployment <deployment-name> --image=<image-name:tag>

2. Run the deployment / expose the deployment
kubectl expose deployment <deployment-name> --type=LoadBalancer --port=8080

3. Scale up/down the deployment
kubectl scale deployment <deployment-name> --replicas=3
//Kubernetes spins up 3 nodes -- all running the same deployment

4. Delete a pod / Delete a running instance
kubectl delete pod <pod-name>
//Kubernetes deployment ensures the replicas would always be 3 even if one of the pod is down/deleted
//It means, it would automatically spins up another pod once a pod is deleted

5. Auto scale/down deployment
kubectl autoscale deployment <deployment-name> --max=10 --cpu-percent=70
//Kubernetes will automatically add/remove instances based on cpu load. it will max add 10 instances

6. New release without going down
kubectl edit deployment <deployment-name>
And add or update minReadySeconds to a desired value

then set the newer image for the release
kubectl set image deployment <deployment-name> <new-image>
//minReadySeconds
