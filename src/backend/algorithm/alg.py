import datetime
from datetime import datetime
from datetime import datetime, timedelta

START_TIME = 0
END_TIME = 1234215

#(time to work, time_to_dedline (in hours))
workds_deadline = [
    (2,10),
    (3,5),
    (6,48),
    (6,42),
]

class timeWorkArray:
    def __init__(self, ls, m) -> None:
        self.MaxTime = m
        self.n = len(ls) + 1
        self.timeArray = [[0 for _ in range(self.n)] for i in range(self.MaxTime)]
        self.workBlocks = ls
        self.worksBools = [False for i in range(self.MaxTime)]
        self.parents =[[[] for _ in range(self.n)] for i in range(self.MaxTime)]
        self.res = None
    
    def set_work_time(self, l,r):
        l += 1
        r += 1
        for i in range(max(0,l),min(len(self.worksBools), r + 1)):
            self.worksBools[i] = True

    def set_mask_time(self, mask, start):
        for i in range(start,len(mask)):
            self.worksBools[i] = self.worksBools[i] | mask[i]
  
    def get_result(self):
        diff = 0
        res_array = []

        isIn = lambda i, ar : i >= 0 and i < len(ar)

        for i in range(len(self.timeArray)):
            while isIn(i + diff, self.worksBools) and self.worksBools[i + diff]:
                res_array.append(None)
                diff += 1
            if isIn(i, self.timeArray):
                res_array.append((self.timeArray[i],self.parents[i]))
        
        return res_array

    def get_max_work_hour_less(self, hour):
        return self.timeArray[hour]
    
    def get_hours(self):
        for i in range(1,len(self.timeArray) - 1):
            if self.timeArray[i] != -float("inf"):
                yield i

    def set_value_by_hour(self, hour, j, value):
        if self.timeArray[hour][j] != -float("inf"):
            self.timeArray[hour][j] = value
    
    def train(self):

        g = self.get_hours()

        vls = set()

        res_val = []
        for i in g:
            for j in range(len(self.workBlocks)):
                if self.workBlocks[j][0] > i :continue
                left = self.get_max_work_hour_less(i + 1 - self.workBlocks[j][0])[j] + 1/self.workBlocks[j][1] ** 2
                right = self.get_max_work_hour_less(i + 1)[j]

                # self.parents[i + 1][j + 1] = self.parents[i + 1 - self.workBlocks[j][0]][j] + [j + 1] if left >= right \
                #     else self.parents[i + 1][j]

                self.parents[i + 1][j + 1] = (i + 1 - self.workBlocks[j][0], j) if left >= right \
                     else self.parents[i + 1][j]

                mx_val =  max(left, right)
                self.set_value_by_hour(i + 1, j + 1, mx_val)
            prs = set(self.parents[i + 1][len(self.parents[i + 1]) - 1]) - vls
            res_val += list(prs)
            vls = prs | vls

        print(4234324)
        pair = self.parents[len(self.parents) - 1][3]
        print(pair)
        print(self.parents[int(pair[0])][int(pair[1])])

        pair = self.parents[int(pair[0])][int(pair[1])]
        print(self.parents[int(pair[0])][int(pair[1])])
        self.res = res_val
    
    def get_res(self):
        return self.res

def predicate_list(list_pred, value):
    for i in list_pred:
        if value >= i[0] and value < i[1]:
            return True
    return False

def get_mask(list_work_time, start_time, end_time):

    duration_minutes = int(((end_time - start_time).total_seconds() + 1) / 60 / 10) + 1
    
    work_schedule = [False] * duration_minutes

    for minute in range(duration_minutes):
        current_time = start_time + timedelta(minutes=minute * 10)

        if predicate_list(list_work_time, current_time.time()) :
            work_schedule[minute + 1] = True
    return work_schedule

def get_parse_data(workBlocks,current_time):
    return [(duration, ((datetime.strptime(date_str, "%Y-%m-%d %H:%M:%S") - current_time).total_seconds()//60//10)) for duration, date_str in workBlocks]

def get_max_deadline(workBlocks):
    return datetime.strptime(max(workBlocks, key=lambda x : datetime.strptime(x[1], "%Y-%m-%d %H:%M:%S"))[1], "%Y-%m-%d %H:%M:%S")

def version_one(workBlocks):


    workTime = [
        (datetime.strptime("14:00", "%H:%M").time(), datetime.strptime("16:00", "%H:%M").time())
    ]

    # process times
    current_time = datetime.now()
    max_deadline = get_max_deadline(workBlocks)
    workBlocks_datetime = get_parse_data(workBlocks, current_time)
    work_schedule = get_mask(workTime, current_time, max_deadline)
    mx_val = max(workBlocks_datetime, key=lambda l : l[1] )
    #mx_deadline =  max(workBlocks_datetime, key=lambda l : l[0] )

    # work algo
    tA = timeWorkArray(workBlocks_datetime, int(mx_val[1]) + 1)
    tA.set_mask_time(work_schedule, 0)
    tA.train()

    total_res = tA.get_result()

    #return total_res[len(total_res) - 1][1][len(workBlocks)]
    for i in range(30):
        print(f"time = {current_time + timedelta(minutes=(i - 1) * 10)}",total_res[i])
    return tA.get_res()


if __name__ == "__main__":

    # blocks:
    workBlocks = [
        (1  * 60 //10,"2023-11-16 20:50:27"),
        (2  * 60 //10,"2023-11-16 20:10:27"),
        (2  * 60 //10,"2023-11-16 20:20:17")
    ]

    version_one(workBlocks)

   