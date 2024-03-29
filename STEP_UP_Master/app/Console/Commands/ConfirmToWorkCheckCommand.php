<?php

namespace App\Console\Commands;

use stdClass;
use App\Models\User;
use App\Models\Service;
use App\Models\UserDetail;
use App\Models\Transaction;
use App\Models\ServiceOrder;
use Illuminate\Support\Carbon;
use Illuminate\Console\Command;
use Illuminate\Support\Collection;
use Illuminate\Support\Facades\Auth;
use App\Http\Controllers\EmailController;
use App\Http\Controllers\MasterController;
use Illuminate\Console\Scheduling\Schedule;

class ConfirmToWorkCheckCommand extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'step_up:confirm-work';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Check if the freelancer accept the order from client within 7 days. so refund and send email';

    /**
     * Execute the console command.
     */
    public function handle()
    {
        // Get the authenticated user
        $emailController = new EmailController();
        $masterController = new MasterController();
        $service_order = ServiceOrder::where('order_status', 0)
        ->where('created_at', '<=', Carbon::now()->subDays(7))
        ->get();
        $collection_transaction = new Collection();

        foreach( $service_order as $order){
            $order->order_status = 4;
            $order->cancel_desc = 'Cancelled by the system, due to freelancer not accepted on time.';
            $order->cancel_at = Carbon::now();
            $order->cancel_by = 1;
            $order->save();
            $service = Service::where('id',$order->service_id)->first();
            $user = User::where('id',$order->order_by)->first();
            //service order cancel status is 4
            if($user && $service){
                //Send Email Part
                $this->sendCancellationEmail($user, $service, $order);
                //Refund part
                UserDetail::where('user_id', $user->id)->increment('balance', $service->price);
            }
            //Append transaction
            $transaction = new Transaction();
            $transaction->user_id = $user->id;
            $transaction->order_id = $order->id;
            $transaction->client_status = 0;
            $transaction->freelancer_status = 1;
            $transaction->isComplain = $order->cancel_desc;
            $transaction->rate = 0;//service rate
            $transaction->tranc_attachments = new stdClass;
            $transaction->tranc_status = 0;
            $transaction->created_by = 1;
            $transaction->updated_by = 1;
            $transaction->created_at = Carbon::now();
            $transaction->updated_at = Carbon::now();
            $collection_transaction->push($transaction);
        }

        if(isset($collection_transaction) && count($collection_transaction) > 0){
            Transaction::insert($collection_transaction->toArray());
        }

        // return var_dump($service_order);
    }
    private function sendCancellationEmail($user, $service, $order)
    {
        $emailController = new EmailController();
        $masterController = new MasterController();

        $subject = 'Service Order Cancellation';
        $content = 'Dear '.$user->name.',' . "\n\n" .
                   'Your service order has been cancelled due to no confirmation from freelancer after 7 days.' . "\n\n" .
                   'Service Details:' . "\n" .
                   'Service ID: ' . $service->id . "\n" .
                   'Service Title: ' . $service->id . "\n" .
                   'Service Description: ' . $service->description . "\n" .
                   'Service Type: ' . $service->service_type . "\n\n" .
                   'Service Requirement: ' . $service->requirement . "\n" .
                   'Service Start Date: ' . $service->start_date . "\n" .
                   'Service End Date: ' . $service->end_date . "\n" .
                   'Status: ' . $masterController->checkServiceStatus($order->order_status) . "\n\n" .
                   'Discount: ' . $service->discount . "%\n\n" .
                   'Price: $' . $service->price . "\n\n" .
                   'This price amount will be refunded 100% no charge. You can check other freelancer that provided similar offer.' . "\n\n" .
                   'Thank you for choosing our platform.';
        $emailController->sendTextEmail($user->email, $subject, $content);
    }
}
