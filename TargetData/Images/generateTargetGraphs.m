function generateTargetGraphs

data = [1000 2000 3000 4000];

allPrediction = [69.4248 71.3729 73.8027 74.6822];
singlePrediction = [36.3449 45.0709 45.9092 48.3338];

figure(1); hold on;
plot(data, singlePrediction, 'r');
plot(data, allPrediction, 'b');
title('Targeting Prediction: 100,000 Test Points');
ylabel('Prediction Accuracy'); xlabel('Number of Training Data');
legend('Train on Evasion Agent', 'Train on All Agents');

percent60 = [63.7622676780386, 69.4441901856089, 71.4998981047483, 72.1505601121454];
percent70 = [65.4438791402762, 71.1521436701657, 79.9935574167463, 80.4034002313767];
percent80 = [80.7766059443912, 83.0502545765479, 85.1861204048566, 86.5860545460585];
percent90 = [87.1107668474051, 87.3629834950233, 88.1215735308262, 89.5520455722424];

figure(2); hold on;
plot(data, percent60, 'r');
plot(data, percent70, 'b');
plot(data, percent80, 'm');
plot(data, percent90, 'g');
title('Targeting Accuracy'); xlabel('Number of Training Data');
ylabel('Accuracy');
legend('Threshold: 60%', 'Threshold: 70%', 'Threshold: 80%', 'Threshold: 90%');

threshold = [60, 70, 80, 90];
best = [72.1505601121454, 80.4034002313767, 86.5860545460585, 89.5520455722424];
worst = [8.41329888663345, 7.49142623415033, 7.18450783553656, 6.51598582831775];
rand = 4.81585491339393*ones(4,1);
straight = 64.357879587431*ones(4,1);

figure(3); hold on;
plot(threshold, best, 'r');
plot(threshold, worst, 'b');
plot(threshold, rand, 'm');
plot(threshold, straight, 'g');
title('Targeting Strategies'); xlabel('Firing Threshold'); ylabel('Accuracy');
legend('Best Firing Angle', 'Worst Firing Angle', 'Random Firing Angle', 'Shoot Direct');

end
