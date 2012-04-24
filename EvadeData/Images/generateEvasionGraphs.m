function generateEvasionGraphs

close all; clear; clc;

% data point training
data = [1000, 2000, 3000, 4000];

% C3PO accuracy
rightC = [72.275334608, 78.964531854, 78.935566162, 78.274445702];
leftC = [68.4834123222749, 83.2653291465318, 86.9500559514695, 87.1387134296273];
haltC = [69.526982011992, 42.0382165605096, 41.4342629482072, 38.7096774193548];
feignC = [70.070607676707, 59.0431738623104, 52.5960539979231, 54.08];

% R2D2 accuracy
rightR = [72.388293293685, 76.6339979722879, 77.5927040827703, 77.6133209990749];
leftR = [73.6902666847164, 80.3008779488135, 82.9054129154162, 82.8242397137746];
haltR = [65.0455927051672, 77.027027027027, 74.4637900580368, 72.83728115345];
feignR = [69.0756619240554, 68.7317852778039, 69.0509453292354, 69.1556903250261];

% Bender accuracy
rightB = [36.8782854832188, 39, 35.9009628610729, 35.3185595567867];
leftB = [30.6584362139918, 46.551724137931, 57.6576576576577, 59.0909090909091];
haltB = [17.8545378764197, 10.7631040792164, 9.36172377826961, 9.59973298415877];
feignB = [25.9786058540026, 13.5913166588013, 15.5747540196784, 15.0648406253787];

% Robbie accuracy (BASELINE)
rightRob = 72.4261811023622; 
leftRob = 73.4947419907068;
haltRob = 38.521057277873;
feignRob = 38.521057277873;

% prediction values
right = [74.0108, 76.0433, 76.5797, 76.9488];
left = [80.0783, 80.9391, 81.4967, 81.7021];
halt = [72.4723, 76.8396, 76.0710, 77.2550];
feign = [69.3406, 71.558, 72.0905, 72.6932];

% C3PO accuracy / life expectancy
c3po = [71.4006917473383, 80.6126903198771, 83.0144649160496, 82.9299990656349;
        448.3383, 573.5897, 620.4400, 618.2107];

% R2D2 accuracy / life expectancy
r2d2 = [70.9933306386419, 77.3690864720238, 58.8744860094273, 58.8744860094273;
        342.7372, 342.7372, 438.1283, 438.1283];

% Bender accuracy / life expectancy
bender = [24.3173031685315, 11.7184427841133, 10.9383057280172, 11.038474350433;
          240.9934, 202.7983, 202.626, 203.9190];
      
% C3PO employment
% 500; 1000; 1500; 2000 
employC = [66421, 2954, 7505, 26626;
           59490, 53183, 157, 1714;
           48608, 67916, 251, 1926;
           47718, 67917, 217, 1875];
       
% Bender employment
employB = [2473, 10206, 19194, 17014;
           500, 58, 37164, 10595; 
           727, 111, 39309, 8334;
           722, 110, 38949, 8251];

% Prediction rather similar
figure(1); hold on;
plot(data, right, 'r');
plot(data, left, 'b');
plot(data, halt, 'g');
plot(data, feign, 'm');
title('Prediction: 20,000 Test Points'); ylabel('Evasion Percentage'); xlabel('Number of Training Data');
legend('Evade Right', 'Evade Left', 'Evade Halt', 'Evade Feign');

% Strategy Employment (Show Baseline of Random)
figure(2); hold on;
plot(data, c3po(1,:), 'r');
plot(data, r2d2(1,:), 'b');
plot(data, bender(1,:), 'g');
plot(data, 58.8744860094273*ones(length(data)), 'm');
title('Evasion Employment Accuracy'); ylabel('Evasion Percentage'); xlabel('Number of Training Data');
legend('Best Strategy', 'Proportional Strategy', 'Worst Strategy', 'Random');

% C3PO individual evasion strategies
figure(4); hold on;
plot(data, rightC, 'r');
plot(data, leftC, 'b');
plot(data, haltC, 'g');
plot(data, feignC, 'm');
title('Best Evasion Strategy Accuracy'); ylabel('Evasion Percentage'); xlabel('Number of Training Data');
legend('Evade Right', 'Evade Left', 'Evade Halt', 'Evade Feign');

% C3PO evasion employment
figure(5); hold on;
employ = ones(4,4);
for i = 1:4
    employ(1,i) = (employC(1,i)/(sum(employC(1,:))))*100;
    employ(2,i) = (employC(2,i)/(sum(employC(2,:))))*100;
    employ(3,i) = (employC(3,i)/(sum(employC(3,:))))*100;
    employ(4,i) = (employC(4,i)/(sum(employC(4,:))))*100;
end
plot(data, employ(:,1), 'r');
plot(data, employ(:,2), 'b');
plot(data, employ(:,3), 'g');
plot(data, employ(:,4), 'm');
title('Best Evasion Strategy Employment'); ylabel('Evasion Percentage'); xlabel('Number of Training Data');
legend('Evade Right', 'Evade Left', 'Evade Halt', 'Evade Feign');

end